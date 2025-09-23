-- ===========================================
-- StrongHub - V2__seed.sql
-- Re-ejecutable: usa ON CONFLICT DO NOTHING
-- Requiere haber aplicado V1__schema.sql
-- ===========================================
BEGIN;

-- 1) Catálogos
INSERT INTO subscription_status (code, description) VALUES
  ('ACTIVE','Active'), ('PAUSED','Paused'),
  ('CANCELED','Canceled'), ('EXPIRED','Expired')
ON CONFLICT DO NOTHING;

INSERT INTO payment_method (code, description) VALUES
  ('CASH','Cash'), ('CARD','Credit/Debit Card'),
  ('TRANSFER','Bank Transfer'), ('ONLINE','Online Gateway')
ON CONFLICT DO NOTHING;

INSERT INTO payment_status (code, description) VALUES
  ('PAID','Paid'), ('PENDING','Pending'),
  ('FAILED','Failed'), ('REFUNDED','Refunded')
ON CONFLICT DO NOTHING;

INSERT INTO booking_status (code, description) VALUES
  ('BOOKED','Booked'), ('CANCELED','Canceled'), ('NO_SHOW','No show')
ON CONFLICT DO NOTHING;

-- 2) Roles / Usuarios (global)
INSERT INTO roles (name) VALUES ('ADMIN'), ('TRAINER'), ('MEMBER')
ON CONFLICT DO NOTHING;

-- Hashes de ejemplo (reemplaza con BCrypt real en tu app)
INSERT INTO users (username, password_hash, enabled) VALUES
 ('admin',    '$2a$10$demoHashAdmin', true),
 ('trainer1', '$2a$10$demoHashTrainer', true),
 ('member1',  '$2a$10$demoHashMember', true)
ON CONFLICT (username) DO NOTHING;

-- (opcional) roles globales
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM (VALUES ('admin','ADMIN'), ('trainer1','TRAINER'), ('member1','MEMBER')) AS x(u, r)
JOIN users  u ON u.username = x.u
JOIN roles  r ON r.name     = x.r
ON CONFLICT DO NOTHING;

-- 3) Gym demo
INSERT INTO gyms (name, country, currency, status, settings)
VALUES ('StrongHub Demo Gym', 'CR', 'USD', 'ACTIVE',
        '{"timezone":"America/Costa_Rica","booking":{"cancelWindowHours":2}}'::jsonb)
ON CONFLICT DO NOTHING;

-- 4) Gym users + roles por gym
WITH ctx AS (
  SELECT id AS gym_id FROM gyms WHERE name = 'StrongHub Demo Gym'
)
INSERT INTO gym_users (gym_id, user_id)
SELECT ctx.gym_id, u.id
FROM ctx
JOIN users u ON u.username IN ('admin','trainer1','member1')
ON CONFLICT DO NOTHING;

INSERT INTO gym_user_roles (gym_user_id, role_id)
SELECT gu.id, r.id
FROM gym_users gu
JOIN gyms g   ON g.id = gu.gym_id AND g.name='StrongHub Demo Gym'
JOIN users u  ON u.id = gu.user_id
JOIN roles r  ON ( (u.username='admin'    AND r.name='ADMIN')
                OR (u.username='trainer1' AND r.name='TRAINER')
                OR (u.username='member1'  AND r.name='MEMBER') )
ON CONFLICT DO NOTHING;

-- 5) Personas globales
INSERT INTO members (first_name, last_name, email, phone, date_of_birth, gender) VALUES
 ('John','Doe','john@example.com','+50670000000','1995-01-10','M'),
 ('Jane','Smith','jane@example.com','+50670000001','1992-07-22','F')
ON CONFLICT (email) DO NOTHING;

INSERT INTO trainers (first_name, last_name, specialization, phone, email) VALUES
 ('Mike','Strong','Strength','+50671000000','mike@gym.com'),
 ('Ana','Flex','Yoga','+50671000001','ana@gym.com')
ON CONFLICT (email) DO NOTHING;

-- 6) Pertenencia al gym
WITH g AS (SELECT id AS gym_id FROM gyms WHERE name='StrongHub Demo Gym')
INSERT INTO gym_members (gym_id, member_id, status, joined_at)
SELECT g.gym_id, m.id, 'ACTIVE', now()
FROM g, members m
WHERE m.email IN ('john@example.com','jane@example.com')
ON CONFLICT DO NOTHING;

WITH g AS (SELECT id AS gym_id FROM gyms WHERE name='StrongHub Demo Gym')
INSERT INTO gym_trainers (gym_id, trainer_id)
SELECT g.gym_id, t.id
FROM g, trainers t
WHERE t.email IN ('mike@gym.com','ana@gym.com')
ON CONFLICT DO NOTHING;

-- 7) Rooms
WITH g AS (SELECT id AS gym_id FROM gyms WHERE name='StrongHub Demo Gym')
INSERT INTO rooms (gym_id, name, capacity, type)
SELECT g.gym_id, v.name, v.capacity, v.type
FROM g
JOIN (VALUES
  ('Room A', 20, 'YOGA'),
  ('Room B', 15, 'HIIT')
) AS v(name, capacity, type) ON TRUE
ON CONFLICT DO NOTHING;

-- 8) Planes del gym
WITH g AS (SELECT id AS gym_id FROM gyms WHERE name='StrongHub Demo Gym')
INSERT INTO plans (gym_id, name, description, price, duration_days)
SELECT g.gym_id, p.name, p.description, p.price, p.duration
FROM g
JOIN (VALUES
  ('Monthly Basic','Access to gym floor', 25.00::numeric, 30),
  ('Monthly Pro','Gym + classes',         40.00::numeric, 30)
) AS p(name, description, price, duration) ON TRUE
ON CONFLICT DO NOTHING;

-- 9) Suscripciones (member ↔ plan dentro del mismo gym)
WITH ctx AS (
  SELECT
    g.id AS gym_id,
    (SELECT id FROM plans WHERE gym_id=g.id AND name='Monthly Basic') AS plan_basic_id,
    (SELECT id FROM plans WHERE gym_id=g.id AND name='Monthly Pro')   AS plan_pro_id,
    (SELECT gm.member_id FROM gym_members gm WHERE gm.gym_id=g.id ORDER BY gm.member_id LIMIT 1) AS member1_id,
    (SELECT gm.member_id FROM gym_members gm WHERE gm.gym_id=g.id ORDER BY gm.member_id OFFSET 1 LIMIT 1) AS member2_id,
    (SELECT gt.trainer_id FROM gym_trainers gt WHERE gt.gym_id=g.id ORDER BY gt.trainer_id LIMIT 1) AS trainer_id
  FROM gyms g WHERE g.name='StrongHub Demo Gym'
)
INSERT INTO subscriptions (gym_id, member_id, plan_id, trainer_id, start_date, end_date, status)
SELECT gym_id, member1_id, plan_basic_id, NULL, CURRENT_DATE, CURRENT_DATE + INTERVAL '30 day', 'ACTIVE' FROM ctx
UNION ALL
SELECT gym_id, member2_id, plan_pro_id,   trainer_id, CURRENT_DATE, CURRENT_DATE + INTERVAL '30 day', 'ACTIVE' FROM ctx
ON CONFLICT DO NOTHING;

-- 10) Clases
WITH ctx AS (
  SELECT
    g.id AS gym_id,
    (SELECT id FROM rooms WHERE gym_id=g.id AND name='Room A') AS roomA_id,
    (SELECT id FROM rooms WHERE gym_id=g.id AND name='Room B') AS roomB_id,
    (SELECT trainer_id FROM gym_trainers WHERE gym_id=g.id ORDER BY trainer_id LIMIT 1) AS trainer1_id,
    (SELECT trainer_id FROM gym_trainers WHERE gym_id=g.id ORDER BY trainer_id OFFSET 1 LIMIT 1) AS trainer2_id
  FROM gyms g WHERE g.name='StrongHub Demo Gym'
)
INSERT INTO class_sessions (gym_id, name, start_time, end_time, room_id, trainer_id, capacity)
SELECT gym_id, 'Morning Yoga', CURRENT_DATE + TIME '08:00', CURRENT_DATE + TIME '09:00', roomA_id, trainer2_id, 20 FROM ctx
UNION ALL
SELECT gym_id, 'HIIT',         CURRENT_DATE + TIME '18:00', CURRENT_DATE + TIME '19:00', roomB_id, trainer1_id, 15 FROM ctx
ON CONFLICT DO NOTHING;

-- 11) Bookings
WITH ctx AS (
  SELECT
    g.id AS gym_id,
    (SELECT id FROM class_sessions WHERE gym_id=g.id AND name='Morning Yoga' LIMIT 1) AS c1_id,
    (SELECT id FROM class_sessions WHERE gym_id=g.id AND name='HIIT'         LIMIT 1) AS c2_id
  FROM gyms g WHERE g.name='StrongHub Demo Gym'
),
mem AS (
  SELECT gm.gym_id, gm.member_id
  FROM gym_members gm
  WHERE gm.gym_id = (SELECT id FROM gyms WHERE name='StrongHub Demo Gym')
  ORDER BY gm.member_id
)
INSERT INTO class_bookings (gym_id, class_id, member_id, status, booked_at)
SELECT (SELECT gym_id FROM ctx), (SELECT c1_id FROM ctx), (SELECT member_id FROM mem LIMIT 1), 'BOOKED', now()
UNION ALL
SELECT (SELECT gym_id FROM ctx), (SELECT c2_id FROM ctx), (SELECT member_id FROM mem OFFSET 1 LIMIT 1), 'BOOKED', now()
ON CONFLICT DO NOTHING;

-- 12) Payments
WITH mem AS (
  SELECT gm.gym_id, gm.member_id
  FROM gym_members gm
  WHERE gm.gym_id = (SELECT id FROM gyms WHERE name='StrongHub Demo Gym')
  ORDER BY gm.member_id
)
INSERT INTO payments (gym_id, member_id, amount, payment_date, method, status)
SELECT (SELECT gym_id FROM mem LIMIT 1), (SELECT member_id FROM mem LIMIT 1),
       25.00::numeric, CURRENT_DATE, 'CASH', 'PAID'
UNION ALL
SELECT (SELECT gym_id FROM mem LIMIT 1), (SELECT member_id FROM mem OFFSET 1 LIMIT 1),
       40.00::numeric, CURRENT_DATE, 'CARD', 'PAID'
ON CONFLICT DO NOTHING;

-- 13) Attendance
WITH ctx AS (
  SELECT
    g.id AS gym_id,
    (SELECT id FROM class_sessions WHERE gym_id=g.id AND name='Morning Yoga' LIMIT 1) AS c1_id,
    (SELECT id FROM class_sessions WHERE gym_id=g.id AND name='HIIT'         LIMIT 1) AS c2_id
  FROM gyms g WHERE g.name='StrongHub Demo Gym'
),
mem AS (
  SELECT gm.member_id
  FROM gym_members gm
  WHERE gm.gym_id = (SELECT id FROM gyms WHERE name='StrongHub Demo Gym')
  ORDER BY gm.member_id
)
INSERT INTO attendance (gym_id, member_id, class_id, check_in)
SELECT (SELECT gym_id FROM ctx), (SELECT member_id FROM mem LIMIT 1), (SELECT c1_id FROM ctx), now()
UNION ALL
SELECT (SELECT gym_id FROM ctx), (SELECT member_id FROM mem OFFSET 1 LIMIT 1), (SELECT c2_id FROM ctx), now()
ON CONFLICT DO NOTHING;

-- 14) B2B: Vendor plans y suscripción del gym a StrongHub
INSERT INTO vendor_plans (name, base_price, member_quota, overage_price, features) VALUES
 ('Starter',    30.00,  200, 0.05, '{"reports":"basic","support":"email"}'),
 ('Pro',        50.00,  500, 0.04, '{"reports":"advanced","support":"priority"}'),
 ('Enterprise',150.00, 2000, 0.03, '{"reports":"full","support":"24x7"}')
ON CONFLICT DO NOTHING;

INSERT INTO gym_vendor_subscriptions (gym_id, vendor_plan_id, start_date, status)
SELECT g.id, vp.id, CURRENT_DATE, 'ACTIVE'
FROM gyms g
JOIN vendor_plans vp ON vp.name='Starter'
WHERE g.name='StrongHub Demo Gym'
ON CONFLICT DO NOTHING;

-- 15) Métricas de ejemplo
INSERT INTO metrics_gym_monthly (gym_id, month, active_members, bookings, storage_gb)
SELECT g.id, date_trunc('month', CURRENT_DATE)::date, 2, 2, 0.1
FROM gyms g WHERE g.name='StrongHub Demo Gym'
ON CONFLICT DO NOTHING;

COMMIT;
