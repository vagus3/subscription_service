INSERT INTO member (id, email, password, name, role, created_at) VALUES
(1, 'user@example.com', '{noop}password123', 'Alex Rivera', 'USER', CURRENT_TIMESTAMP),
(2, 'admin@example.com', '{noop}admin123', 'Admin', 'ADMIN', CURRENT_TIMESTAMP);

INSERT INTO subscription_plan (id, name, category, monthly_price, yearly_price, provider_url) VALUES
(1, 'Netflix', 'Premium 4K', 19, 228, 'https://www.netflix.com'),
(2, 'Spotify', 'Family Plan', 15, 180, 'https://www.spotify.com'),
(3, 'AWS Cloud', 'Developer Instance', 245, 2940, 'https://aws.amazon.com'),
(4, 'Adobe CC', 'Photography', 52, 624, 'https://www.adobe.com'),
(5, 'iCloud+', '2TB Family', 9, 108, 'https://www.icloud.com'),
(6, 'Amazon Prime', 'Annual Plan', 139, 139, 'https://www.amazon.com/prime');

INSERT INTO member_subscription (id, member_id, plan_id, start_date, due_date, status, last_used_at, alert_level) VALUES
(1, 1, 1, '2023-01-12', DATEADD('DAY', 3, CURRENT_DATE), 'ACTIVE', DATEADD('DAY', -1, CURRENT_DATE), 'NORMAL'),
(2, 1, 2, '2022-03-05', DATEADD('DAY', 15, CURRENT_DATE), 'ACTIVE', DATEADD('DAY', -9, CURRENT_DATE), 'WARNING'),
(3, 1, 3, '2021-10-28', DATEADD('DAY', 23, CURRENT_DATE), 'ACTIVE', DATEADD('DAY', -22, CURRENT_DATE), 'DANGER'),
(4, 1, 4, '2023-06-10', DATEADD('DAY', 6, CURRENT_DATE), 'CANCELLED', DATEADD('DAY', -64, CURRENT_DATE), 'CRITICAL');

INSERT INTO subscription_usage (member_subscription_id, used_date, used, duration_minutes) VALUES
(1, DATEADD('DAY', -1, CURRENT_DATE), TRUE, 42),
(1, DATEADD('DAY', -2, CURRENT_DATE), TRUE, 38),
(2, DATEADD('DAY', -9, CURRENT_DATE), TRUE, 25),
(3, DATEADD('DAY', -22, CURRENT_DATE), TRUE, 60);
