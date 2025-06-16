CREATE TABLE IF NOT EXISTS users (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_superadmin TINYINT(1) NOT NULL DEFAULT 0,
    is_admin TINYINT(1) NOT NULL DEFAULT 1,
    permission VARCHAR(18) NOT NULL, /* create-read-delete or create-read or read */
    otp INT(6) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT users_email_unique UNIQUE(email),
    CONSTRAINT users_otp_unique UNIQUE(otp),
    INDEX users_otp_index (otp),
    INDEX users_email_index (email)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS questions (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    question VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT questions_question_unique UNIQUE(question)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS halls (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT halls_name_unique UNIQUE(name),
    CONSTRAINT halls_slug_unique UNIQUE(slug),
    INDEX halls_slug_index (slug)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS stalls (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    hall_id INT UNSIGNED NOT NULL,
    logo VARCHAR(255) NULL,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT stalls_hall_id_foreign FOREIGN KEY (hall_id) REFERENCES halls(id),
    CONSTRAINT stalls_hall_id_name_slug_unique UNIQUE(hall_id, name, slug)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS barcodes (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    code INT UNSIGNED NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT barcodes_code_unique UNIQUE(code),
    INDEX barcodes_code_index (code)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS votes (
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    hall_id INT UNSIGNED NOT NULL,
    stall_id INT UNSIGNED NOT NULL,
    question_id INT UNSIGNED NOT NULL,
    barcode_id INT UNSIGNED NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT votes_hall_id_question_id_barcode_id_unique UNIQUE (hall_id, question_id, barcode_id),
    CONSTRAINT votes_hall_id_foreign FOREIGN KEY (hall_id) REFERENCES halls(id),
    CONSTRAINT votes_stall_id_foreign FOREIGN KEY (stall_id) REFERENCES stalls(id),
    CONSTRAINT votes_question_id_foreign FOREIGN KEY (question_id) REFERENCES questions(id),
    CONSTRAINT votes_barcode_id_foreign FOREIGN KEY (barcode_id) REFERENCES barcodes(id)
) ENGINE=InnoDB;

/* Insert */
INSERT INTO users (name, email, permission, password, is_superadmin) VALUES (
    "Betafore Admin", "admin@betafore.com", "create-read-delete", "$2a$10$RuEibDNqmzmrWSFldd5OreJTgTL0c7hJaKz50TqDe7vzHmiFPcW46", "1"
);
