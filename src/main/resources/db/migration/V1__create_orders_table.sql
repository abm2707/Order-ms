CREATE TABLE orders (
    id UUID NOT NULL,
    order_number VARCHAR(50) NOT NULL,
    customer_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    total_amount NUMERIC(19,4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT,

    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT uk_orders_order_number UNIQUE (order_number)
);

CREATE INDEX idx_orders_customer_id ON orders (customer_id);
CREATE INDEX idx_orders_status ON orders (status);
CREATE INDEX idx_orders_created_at ON orders (created_at);