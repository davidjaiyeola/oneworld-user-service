CREATE TABLE IF NOT EXISTS  `user` (
`id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
`verified` bit NOT NULL,
`created_at` datetime(6),
    `date_deactivated` datetime(6),
    `date_registered` datetime(6),
    `date_verified` datetime(6),
    `email` varchar(255),
    `firstname` varchar(255),
    `lastname` varchar(255),
    `mobile` varchar(255),
    `password` varchar(255),
    `role` varchar(100),
    `status` varchar(100),
    `title` varchar(255),
    `updated_at` datetime(6)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 DEFAULT COLLATE=utf8mb4_0900_ai_ci;

