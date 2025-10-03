-- V5__protect_soft_delete_plan_in_use.sql
CREATE OR REPLACE FUNCTION prevent_soft_delete_plan_in_use()
RETURNS trigger AS $$
BEGIN
  -- si intenta marcar deleted_at y el plan está en uso, bloquear
  IF NEW.deleted_at IS NOT NULL AND OLD.deleted_at IS NULL THEN
    IF EXISTS (
      SELECT 1 FROM gym_members gm
      WHERE gm.gym_id = OLD.gym_id AND gm.plan_id = OLD.id AND gm.deleted_at IS NULL
    ) OR EXISTS (
      SELECT 1 FROM subscriptions s
      WHERE s.gym_id = OLD.gym_id AND s.plan_id = OLD.id AND s.deleted_at IS NULL
    ) THEN
      RAISE EXCEPTION 'No se puede soft-borrar el plan % (gym %): está en uso.', OLD.id, OLD.gym_id;
    END IF;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_plans_soft_delete_guard ON plans;
CREATE TRIGGER trg_plans_soft_delete_guard
BEFORE UPDATE ON plans
FOR EACH ROW
EXECUTE FUNCTION prevent_soft_delete_plan_in_use();
