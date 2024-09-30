CREATE TABLE attachments (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    KEY TEXT
);

ALTER TABLE dishes
ADD COLUMN attachment_id UUID UNIQUE;

ALTER TABLE dishes
ADD CONSTRAINT fk_attachments
FOREIGN KEY (attachment_id)
REFERENCES attachments (id);