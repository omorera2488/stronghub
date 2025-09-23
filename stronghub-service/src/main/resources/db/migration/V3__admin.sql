-- ============================================
-- StrongHub - V3__admin.sql
-- Usuario administrador global + rol ADMIN
-- ============================================

-- Hash bcrypt para "Admin#2024!"
-- Generado con BCrypt (cost=10)
-- Puedes regenerar con: 
--   new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("Admin#2024!")
-- en una app Spring Boot.
DO $$
BEGIN
  -- Inserta usuario admin si no existe
  INSERT INTO users (username, password_hash, enabled)
  VALUES ('admin', '{bcrypt}$2a$10$J61M.6.3mK33UbCqOau3Eu6/nZfiUI8jqKgvfKr98VnCJ5USdN9iG', TRUE)
  ON CONFLICT (username) DO NOTHING;

  -- Asigna rol global ADMIN
  INSERT INTO user_roles (user_id, role_id)
  SELECT u.id, r.id
  FROM users u, roles r
  WHERE u.username = 'admin'
    AND r.name = 'ADMIN'
  ON CONFLICT DO NOTHING;

  -- Asigna también al gym demo (si existe) como GYM_OWNER
  INSERT INTO gym_users (gym_id, user_id)
  SELECT g.id, u.id
  FROM gyms g, users u
  WHERE g.name = 'StrongHub Demo Gym'
    AND u.username = 'admin'
  ON CONFLICT (gym_id, user_id) DO NOTHING;

  INSERT INTO gym_user_roles (gym_user_id, role_id)
  SELECT gu.id, r.id
  FROM gym_users gu
  JOIN users u ON gu.user_id = u.id
  JOIN gyms g ON gu.gym_id = g.id
  JOIN roles r ON r.name = 'GYM_OWNER'
  WHERE u.username = 'admin'
    AND g.name = 'StrongHub Demo Gym'
  ON CONFLICT (gym_user_id, role_id) DO NOTHING;
END $$;
