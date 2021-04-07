CREATE TABLE IF NOT EXISTS `verification_token`
(`id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
`activated` bit NOT NULL,
`expired` bit NOT NULL,
`created_at` datetime(6),
    `expiryDate` datetime(6),
    `user_id` bigint NOT NULL,
    `date_verified` datetime(6),
    `confirmation_token` varchar(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 DEFAULT COLLATE=utf8mb4_0900_ai_ci;

