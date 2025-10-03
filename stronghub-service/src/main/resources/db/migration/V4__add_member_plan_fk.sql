-- 1) Columna nullable
ALTER TABLE gym_members ADD COLUMN IF NOT EXISTS plan_id BIGINT;

-- 2) Backfill: elegir un plan ACTIVO (deleted_at IS NULL) por gym
WITH chosen AS (
  SELECT DISTINCT ON (p.gym_id) p.gym_id, p.id AS plan_id
  FROM plans p
  WHERE p.deleted_at IS NULL
  ORDER BY p.gym_id, p.id
)
UPDATE gym_members gm
SET plan_id = c.plan_id
FROM chosen c
WHERE gm.gym_id = c.gym_id
  AND gm.plan_id IS NULL;

-- 2.1) Verifica que no queden members sin plan (si falta, crea un plan por ese gym o falla)
DO $$
DECLARE v_missing int;
BEGIN
  SELECT COUNT(*) INTO v_missing FROM gym_members WHERE plan_id IS NULL;
  IF v_missing > 0 THEN
    RAISE EXCEPTION 'Existen % gym_members sin plan activo. Cree al menos un plan (deleted_at IS NULL) por gym.', v_missing;
  END IF;
END$$;

-- 3) Unique compuesto requerido por el FK
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'uq_plans_id_gym') THEN
    ALTER TABLE plans ADD CONSTRAINT uq_plans_id_gym UNIQUE (id, gym_id);
  END IF;
END$$;

-- 4) FK compuesto (mismo gym). NOT VALID para rapidez; luego VALIDATE.
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_gym_members_plan') THEN
    ALTER TABLE gym_members
      ADD CONSTRAINT fk_gym_members_plan
      FOREIGN KEY (plan_id, gym_id)
      REFERENCES plans (id, gym_id)
      NOT VALID;
  END IF;
END$$;

ALTER TABLE gym_members ALTER COLUMN plan_id SET NOT NULL;
ALTER TABLE gym_members VALIDATE CONSTRAINT fk_gym_members_plan;

CREATE INDEX IF NOT EXISTS ix_gym_members_plan ON gym_members(plan_id);
