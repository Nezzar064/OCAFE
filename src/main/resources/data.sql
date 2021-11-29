REPLACE INTO `role` VALUES (1, 'ROLE_ADMIN');
REPLACE INTO `role` VALUES (2, 'ROLE_MODERATOR');
REPLACE INTO `role` VALUES (3, 'ROLE_USER');

-- INSERT INTO `account` (`account_id`, `account_name`, `account_password`, `active`) VALUES (1, 'admin', '$2a$12$iUGLoSAYqQFdBxCmw60z6eUwLJLhsmx4uundoSA8wkzKrdS5g1jCG', 1);
-- INSERT INTO `account` (`account_id`, `account_name`, `account_password`, `active`) VALUES (2, 'secretary', '$2a$12$iUGLoSAYqQFdBxCmw60z6eUwLJLhsmx4uundoSA8wkzKrdS5g1jCG', 1);
-- INSERT INTO `account_role` (`account_id`, `role_id`) VALUES (1, 1);
-- INSERT INTO `account_role` (`account_id`, `role_id`) VALUES (2, 2);

-- hash value is password, in case you forget dummy.

-- INSERT INTO `users` (`user_id`, `user_name`, `password`, `active`, `person_id`) VALUES (1, 'alex', '$2a$10$EZ1TVvvwsiHGnjXtmeMOQuJFlUXpxI4kjOGdOPhLD6o9kAzWqh38y', 1, 1);
