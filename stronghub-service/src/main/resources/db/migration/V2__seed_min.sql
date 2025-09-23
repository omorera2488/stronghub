-- ============================================
-- StrongHub - V2__seed_min.sql (FIX)
-- Semillas mínimas (roles + catálogos + demo opcional)
-- Idempotente para PostgreSQL 13+
-- ============================================

-- 1) ROLES (global)
INSERT INTO roles (name)
VALUES
  ('ADMIN'),
  ('GYM_OWNER'),
  ('TRAINER'),
  ('MEMBER')
ON CONFLICT (name) DO NOTHING;

-- 2) CATÁLOGOS

-- 2.1) subscription_status
INSERT INTO subscription_status (code, description)
VALUES
  ('ACTIVE',   'Membership currently active'),
  ('PAUSED',   'Temporarily paused / on hold'),
  ('CANCELED', 'Canceled before end date'),
  ('EXPIRED',  'Reached end date and expired')
ON CONFLICT (code) DO UPDATE SET description = EXCLUDED.description;

-- 2.2) payment_method
INSERT INTO payment_method (code, description)
VALUES
  ('CASH',     'Cash payment'),
  ('CARD',     'Debit/Credit card'),
  ('TRANSFER', 'Bank transfer'),
  ('OTHER',    'Other method')
ON CONFLICT (code) DO UPDATE SET description = EXCLUDED.description;

-- 2.3) payment_status
INSERT INTO payment_status (code, description)
VALUES
  ('PENDING',  'Awaiting confirmation'),
  ('PAID',     'Payment completed'),
  ('FAILED',   'Payment failed'),
  ('REFUNDED', 'Payment refunded')
ON CONFLICT (code) DO UPDATE SET description = EXCLUDED.description;

-- 2.4) booking_status
INSERT INTO booking_status (code, description)
VALUES
  ('BOOKED',    'Reservation confirmed'),
  ('CANCELLED', 'Reservation cancelled'),
  ('ATTENDED',  'Member attended the class'),
  ('NO_SHOW',   'Member did not attend')
ON CONFLICT (code) DO UPDATE SET description = EXCLUDED.description;

-- 3) GYM DEMO (opcional) + asignación a admin si existe
--    Evitamos CTEs complejos y usamos un bloque DO idempotente.

DO $$
DECLARE
  v_gym_id       INT;
  v_user_id      INT;
  v_role_owner   INT;
  v_gym_user_id  INT;
BEGIN
  -- Obtener o crear el gym demo
  SELECT id INTO v_gym_id
  FROM gyms
  WHERE name = 'StrongHub Demo Gym';

  IF v_gym_id IS NULL THEN
    INSERT INTO gyms (name, country, currency, status, settings)
    VALUES ('StrongHub Demo Gym', 'CR', 'USD', 'ACTIVE', '{}'::jsonb)
    RETURNING id INTO v_gym_id;
  END IF;

  -- Vincular al usuario 'admin' si existe
  SELECT id INTO v_user_id
  FROM users
  WHERE username = 'admin';

  IF v_user_id IS NOT NULL THEN
    -- Crear vínculo gym-user si no existe
    INSERT INTO gym_users (gym_id, user_id)
    VALUES (v_gym_id, v_user_id)
    ON CONFLICT (gym_id, user_id) DO NOTHING
    RETURNING id INTO v_gym_user_id;

    IF v_gym_user_id IS NULL THEN
      SELECT id INTO v_gym_user_id
      FROM gym_users
      WHERE gym_id = v_gym_id AND user_id = v_user_id;
    END IF;

    -- Rol GYM_OWNER
    SELECT id INTO v_role_owner
    FROM roles
    WHERE name = 'GYM_OWNER';

    IF v_role_owner IS NOT NULL AND v_gym_user_id IS NOT NULL THEN
      INSERT INTO gym_user_roles (gym_user_id, role_id)
      VALUES (v_gym_user_id, v_role_owner)
      ON CONFLICT DO NOTHING;
    END IF;
  END IF;
END $$;
