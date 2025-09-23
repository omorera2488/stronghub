-- ============================================
-- StrongHub - V1__schema.sql  (PostgreSQL 13+)
-- Multi-tenant + Auditoría + Catálogos + B2B
-- ============================================

-- 0) Función de auditoría (updated_at)
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS trigger AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- =================================
-- 1) Seguridad (global)
-- =================================
CREATE TABLE roles (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) UNIQUE NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL
);
CREATE TRIGGER trg_roles_updated_at BEFORE UPDATE ON roles
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(80) UNIQUE NOT NULL,
  password_hash VARCHAR(200) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL
);
CREATE TRIGGER trg_users_updated_at BEFORE UPDATE ON users
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TABLE user_roles (
  user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  role_id INT NOT NULL REFERENCES roles(id) ON DELETE RESTRICT,
  PRIMARY KEY (user_id, role_id),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL
);
CREATE TRIGGER trg_user_roles_updated_at BEFORE UPDATE ON user_roles
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- =================================
-- 2) Tenancy
-- =================================
CREATE TABLE gyms (
  id SERIAL PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  country VARCHAR(2),
  currency VARCHAR(3) DEFAULT 'USD',
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  settings JSONB DEFAULT '{}'::jsonb,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL
);
CREATE TRIGGER trg_gyms_updated_at BEFORE UPDATE ON gyms
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TABLE gym_users (
  id SERIAL PRIMARY KEY,
  gym_id INT NOT NULL REFERENCES gyms(id) ON DELETE CASCADE,
  user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  UNIQUE (gym_id, user_id),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL
);
CREATE TRIGGER trg_gym_users_updated_at BEFORE UPDATE ON gym_users
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TABLE gym_user_roles (
  gym_user_id INT NOT NULL REFERENCES gym_users(id) ON DELETE CASCADE,
  role_id INT NOT NULL REFERENCES roles(id) ON DELETE RESTRICT,
  PRIMARY KEY (gym_user_id, role_id),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL
);
CREATE TRIGGER trg_gym_user_roles_updated_at BEFORE UPDATE ON gym_user_roles
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- =================================
-- 3) Personas (global) + pertenencia por gym
-- =================================
CREATE TABLE members (
  id SERIAL PRIMARY KEY,
  first_name VARCHAR(80) NOT NULL,
  last_name  VARCHAR(80) NOT NULL,
  email VARCHAR(120) UNIQUE NOT NULL,
  phone VARCHAR(40),
  date_of_birth DATE,
  gender VARCHAR(20),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL
);
CREATE TRIGGER trg_members_updated_at BEFORE UPDATE ON members
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- índices útiles
CREATE UNIQUE INDEX IF NOT EXISTS uq_members_email_active
  ON members(email) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_members_last_name_active
  ON members(last_name) WHERE deleted_at IS NULL;

CREATE TABLE trainers (
  id SERIAL PRIMARY KEY,
  first_name VARCHAR(80) NOT NULL,
  last_name  VARCHAR(80) NOT NULL,
  specialization VARCHAR(120),
  phone VARCHAR(40),
  email VARCHAR(120),
  UNIQUE (email),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL
);
CREATE TRIGGER trg_trainers_updated_at BEFORE UPDATE ON trainers
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE INDEX IF NOT EXISTS idx_trainers_email_active
  ON trainers(email) WHERE deleted_at IS NULL;

CREATE TABLE gym_members (
  id SERIAL PRIMARY KEY,
  gym_id INT NOT NULL REFERENCES gyms(id) ON DELETE CASCADE,
  member_id INT NOT NULL REFERENCES members(id) ON DELETE CASCADE,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  joined_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE (gym_id, member_id),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL
);
CREATE TRIGGER trg_gym_members_updated_at BEFORE UPDATE ON gym_members
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TABLE gym_trainers (
  id SERIAL PRIMARY KEY,
  gym_id INT NOT NULL REFERENCES gyms(id) ON DELETE CASCADE,
  trainer_id INT NOT NULL REFERENCES trainers(id) ON DELETE CASCADE,
  UNIQUE (gym_id, trainer_id),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL
);
CREATE TRIGGER trg_gym_trainers_updated_at BEFORE UPDATE ON gym_trainers
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- =================================
-- 4) Catálogos (enum-like)
-- =================================
CREATE TABLE subscription_status (
  code VARCHAR(20) PRIMARY KEY,
  description TEXT
);

CREATE TABLE payment_method (
  code VARCHAR(20) PRIMARY KEY,
  description TEXT
);

CREATE TABLE payment_status (
  code VARCHAR(20) PRIMARY KEY,
  description TEXT
);

CREATE TABLE booking_status (
  code VARCHAR(20) PRIMARY KEY,
  description TEXT
);

-- =================================
-- 5) Rooms / Locations
-- =================================
CREATE TABLE rooms (
  id SERIAL PRIMARY KEY,
  gym_id INT NOT NULL REFERENCES gyms(id) ON DELETE CASCADE,
  name VARCHAR(120) NOT NULL,
  capacity INT,
  type VARCHAR(40),
  UNIQUE (gym_id, name),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL
);
CREATE TRIGGER trg_rooms_updated_at BEFORE UPDATE ON rooms
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- =================================
-- 6) Productos del gym (planes) y suscripciones
-- =================================
CREATE TABLE plans (
  id SERIAL PRIMARY KEY,
  gym_id INT NOT NULL REFERENCES gyms(id) ON DELETE CASCADE,
  name VARCHAR(80) NOT NULL,
  description TEXT,
  price NUMERIC(12,2) NOT NULL,
  duration_days INT NOT NULL,
  UNIQUE (gym_id, name),
  UNIQUE (gym_id, id),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL
);
CREATE TRIGGER trg_plans_updated_at BEFORE UPDATE ON plans
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- índice auxiliar para FKs compuestas (gym_id, id)
CREATE INDEX IF NOT EXISTS idx_plans_gym_id_id ON plans(gym_id, id);

CREATE TABLE subscriptions (
  id SERIAL PRIMARY KEY,
  gym_id INT NOT NULL REFERENCES gyms(id) ON DELETE CASCADE,
  member_id INT NOT NULL,
  plan_id INT NOT NULL,
  trainer_id INT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL,
  CONSTRAINT fk_sub_gym_member FOREIGN KEY (gym_id, member_id)
    REFERENCES gym_members(gym_id, member_id),
  CONSTRAINT fk_sub_gym_plan FOREIGN KEY (gym_id, plan_id)
    REFERENCES plans(gym_id, id),
  CONSTRAINT fk_sub_status FOREIGN KEY (status)
    REFERENCES subscription_status(code)
);
CREATE TRIGGER trg_subscriptions_updated_at BEFORE UPDATE ON subscriptions
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE INDEX IF NOT EXISTS idx_subscriptions_gym_member_status_end_active
  ON subscriptions(gym_id, member_id, status, end_date)
  WHERE deleted_at IS NULL;

-- =================================
-- 7) Clases, reservas, asistencia
-- =================================
CREATE TABLE class_sessions (
  id SERIAL PRIMARY KEY,
  gym_id INT NOT NULL REFERENCES gyms(id) ON DELETE CASCADE,
  name VARCHAR(120) NOT NULL,
  start_time TIMESTAMPTZ NOT NULL,
  end_time   TIMESTAMPTZ NOT NULL,
  room_id INT NOT NULL REFERENCES rooms(id) ON DELETE RESTRICT,
  trainer_id INT NULL,
  capacity INT NOT NULL,
  UNIQUE (gym_id, id),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL
);
CREATE TRIGGER trg_class_sessions_updated_at BEFORE UPDATE ON class_sessions
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- Si deseas forzar que trainer pertenezca al mismo gym, podrías usar triggers,
-- o agregar columna gym_id en gym_trainers y FK compuesta (ya existe), y validar a nivel app.

CREATE INDEX IF NOT EXISTS idx_class_sessions_gym_start_active
  ON class_sessions(gym_id, start_time) WHERE deleted_at IS NULL;

CREATE TABLE class_bookings (
  id SERIAL PRIMARY KEY,
  gym_id INT NOT NULL REFERENCES gyms(id) ON DELETE CASCADE,
  class_id INT NOT NULL,
  member_id INT NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'BOOKED',
  booked_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL,
  CONSTRAINT fk_bookings_gym_class FOREIGN KEY (gym_id, class_id)
    REFERENCES class_sessions(gym_id, id) ON DELETE CASCADE,
  CONSTRAINT fk_bookings_gym_member FOREIGN KEY (gym_id, member_id)
    REFERENCES gym_members(gym_id, member_id) ON DELETE CASCADE,
  CONSTRAINT fk_booking_status FOREIGN KEY (status)
    REFERENCES booking_status(code)
);
CREATE TRIGGER trg_class_bookings_updated_at BEFORE UPDATE ON class_bookings
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE UNIQUE INDEX IF NOT EXISTS uq_bookings_gym_class_member_active
  ON class_bookings(gym_id, class_id, member_id)
  WHERE deleted_at IS NULL;

CREATE TABLE attendance (
  id SERIAL PRIMARY KEY,
  gym_id INT NOT NULL REFERENCES gyms(id) ON DELETE CASCADE,
  member_id INT NOT NULL,
  class_id INT NULL,
  check_in TIMESTAMPTZ NOT NULL,
  check_out TIMESTAMPTZ NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL,
  CONSTRAINT fk_att_gym_member FOREIGN KEY (gym_id, member_id)
    REFERENCES gym_members(gym_id, member_id) ON DELETE CASCADE,
  CONSTRAINT fk_att_gym_class FOREIGN KEY (gym_id, class_id)
    REFERENCES class_sessions(gym_id, id) ON DELETE SET NULL
);
CREATE TRIGGER trg_attendance_updated_at BEFORE UPDATE ON attendance
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE INDEX IF NOT EXISTS idx_attendance_gym_member_in_active
  ON attendance(gym_id, member_id, check_in) WHERE deleted_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_attendance_gym_class_in_active
  ON attendance(gym_id, class_id, check_in) WHERE deleted_at IS NULL;

-- =================================
-- 8) Pagos (gym -> member)
-- =================================
CREATE TABLE payments (
  id SERIAL PRIMARY KEY,
  gym_id INT NOT NULL REFERENCES gyms(id) ON DELETE CASCADE,
  member_id INT NOT NULL,
  amount NUMERIC(12,2) NOT NULL,
  payment_date DATE NOT NULL,
  method VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL,
  CONSTRAINT fk_pay_gym_member FOREIGN KEY (gym_id, member_id)
    REFERENCES gym_members(gym_id, member_id) ON DELETE CASCADE,
  CONSTRAINT fk_pay_method FOREIGN KEY (method)
    REFERENCES payment_method(code),
  CONSTRAINT fk_pay_status FOREIGN KEY (status)
    REFERENCES payment_status(code)
);
CREATE TRIGGER trg_payments_updated_at BEFORE UPDATE ON payments
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE INDEX IF NOT EXISTS idx_payments_gym_member_date_active
  ON payments(gym_id, member_id, payment_date) WHERE deleted_at IS NULL;

-- =================================
-- 9) B2B (tus planes para los gyms)
-- =================================
CREATE TABLE vendor_plans (
  id SERIAL PRIMARY KEY,
  name VARCHAR(80) UNIQUE NOT NULL,            -- Starter, Pro, Enterprise
  base_price NUMERIC(12,2) NOT NULL DEFAULT 0,
  member_quota INT NOT NULL DEFAULT 200,
  overage_price NUMERIC(12,2) DEFAULT 0,
  features JSONB DEFAULT '{}'::jsonb,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL
);
CREATE TRIGGER trg_vendor_plans_updated_at BEFORE UPDATE ON vendor_plans
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TABLE gym_vendor_subscriptions (
  id SERIAL PRIMARY KEY,
  gym_id INT NOT NULL REFERENCES gyms(id) ON DELETE CASCADE,
  vendor_plan_id INT NOT NULL REFERENCES vendor_plans(id),
  start_date DATE NOT NULL,
  end_date DATE,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, PAUSED, CANCELED
  custom_base_price NUMERIC(12,2),
  custom_member_quota INT,
  custom_overage_price NUMERIC(12,2),
  UNIQUE (gym_id, vendor_plan_id, start_date),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  created_by INT NULL,
  updated_by INT NULL,
  deleted_at TIMESTAMPTZ NULL
);
CREATE TRIGGER trg_gym_vendor_subscriptions_updated_at
  BEFORE UPDATE ON gym_vendor_subscriptions
  FOR EACH ROW EXECUTE FUNCTION set_updated_at();

CREATE TABLE metrics_gym_monthly (
  id SERIAL PRIMARY KEY,
  gym_id INT NOT NULL REFERENCES gyms(id) ON DELETE CASCADE,
  month DATE NOT NULL,                     -- primer día del mes
  active_members INT NOT NULL DEFAULT 0,
  bookings INT NOT NULL DEFAULT 0,
  storage_gb NUMERIC(10,2) DEFAULT 0,
  UNIQUE (gym_id, month),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- =========================
-- FIN V1__schema.sql
-- =========================
