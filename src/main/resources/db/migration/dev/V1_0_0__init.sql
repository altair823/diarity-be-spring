CREATE TABLE `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `name` varchar(255) DEFAULT NULL,
                         `display_name` varchar(255) NOT NULL,
                         `email` varchar(255) NOT NULL,
                         `picture` varchar(255) DEFAULT NULL,
                         `role` varchar(255) NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `posts` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `comments_count` int DEFAULT NULL,
                         `content` mediumtext,
                         `created_at` datetime(6) DEFAULT NULL,
                         `deleted_at` datetime(6) DEFAULT NULL,
                         `is_deleted` bit(1) DEFAULT NULL,
                         `is_public` bit(1) DEFAULT NULL,
                         `likes_count` int DEFAULT NULL,
                         `modified_at` datetime(6) DEFAULT NULL,
                         `title` varchar(255) DEFAULT NULL,
                         `author_id` bigint DEFAULT NULL,
                         `book_title` varchar(255) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         KEY `FK6xvn0811tkyo3nfjk2xvqx6ns` (`author_id`),
                         CONSTRAINT `FK6xvn0811tkyo3nfjk2xvqx6ns` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `comments` (
                            `is_deleted` bit(1) DEFAULT NULL,
                            `likes_count` int DEFAULT NULL,
                            `author_id` bigint DEFAULT NULL,
                            `created_at` datetime(6) DEFAULT NULL,
                            `deleted_at` datetime(6) DEFAULT NULL,
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `modified_at` datetime(6) DEFAULT NULL,
                            `post_id` bigint DEFAULT NULL,
                            `content` text,
                            PRIMARY KEY (`id`),
                            KEY `FKn2na60ukhs76ibtpt9burkm27` (`author_id`),
                            KEY `FKh4c7lvsc298whoyd4w9ta25cr` (`post_id`),
                            CONSTRAINT `FKh4c7lvsc298whoyd4w9ta25cr` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
                            CONSTRAINT `FKn2na60ukhs76ibtpt9burkm27` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `comments_hierarchy` (
                                      `child_comments_id` bigint NOT NULL,
                                      `parent_comments_id` bigint NOT NULL,
                                      PRIMARY KEY (`child_comments_id`,`parent_comments_id`),
                                      KEY `FK9o3dxmex4sgcxme9osc8wx05l` (`parent_comments_id`),
                                      CONSTRAINT `FK9o3dxmex4sgcxme9osc8wx05l` FOREIGN KEY (`parent_comments_id`) REFERENCES `comments` (`id`),
                                      CONSTRAINT `FKp3mn57khswa853cnnh8lukph7` FOREIGN KEY (`child_comments_id`) REFERENCES `comments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `comments_likes` (
                                  `comment_id` bigint NOT NULL,
                                  `user_id` bigint NOT NULL,
                                  PRIMARY KEY (`comment_id`,`user_id`),
                                  KEY `FKiwf3mhli7ej3pgf9ktj6vv08p` (`user_id`),
                                  CONSTRAINT `FKiwf3mhli7ej3pgf9ktj6vv08p` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                                  CONSTRAINT `FKogmkq8clqlxqis53e9tlu4w96` FOREIGN KEY (`comment_id`) REFERENCES `comments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `likes` (
                         `created_at` datetime(6) DEFAULT NULL,
                         `user_id` bigint NOT NULL,
                         `post_id` bigint NOT NULL,
                         PRIMARY KEY (`post_id`,`user_id`),
                         KEY `FKnvx9seeqqyy71bij291pwiwrg` (`user_id`),
                         CONSTRAINT `FKnvx9seeqqyy71bij291pwiwrg` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
                         CONSTRAINT `FKry8tnr4x2vwemv2bb0h5hyl0x` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `posts_likes` (
                               `created_at` datetime(6) DEFAULT NULL,
                               `post_id` bigint NOT NULL,
                               `user_id` bigint NOT NULL,
                               PRIMARY KEY (`post_id`,`user_id`),
                               KEY `FKt5kx9tu4bo443unk2n21dmshd` (`user_id`),
                               CONSTRAINT `FKimxtd6dl39nmu9x0snqm6mu1g` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
                               CONSTRAINT `FKt5kx9tu4bo443unk2n21dmshd` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


