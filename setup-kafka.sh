#!/bin/bash

# Kafka Configurations
KAFKA_BIN="/home/eleven/Tools/kafka_2.13-3.9.0/bin"
BOOTSTRAP_SERVER="localhost:9092"

# Topics
declare -a TOPICS=("verification-email-topic" "reset-password-email-topic")

# Consumer KAFKA GROUPS (must match the topics order)
declare -a KAFKA_GROUPS=("verification-email-group" "reset-password-group")  # Force it to be an array

# Function to create Kafka topics
create_topics() {
    echo "Creating Kafka topics..."
    for topic in "${TOPICS[@]}"; do
        $KAFKA_BIN/kafka-topics.sh --create --topic "$topic" \
            --bootstrap-server "$BOOTSTRAP_SERVER" \
            --partitions 1 --replication-factor 1 \
            --if-not-exists
    done
    echo "✅ Kafka topics created successfully!"
}

# Function to create consumer KAFKA_GROUPS
create_groups() {
    echo "Creating Kafka consumer groups..."

    # Debugging: Print the arrays
    echo "DEBUG: TOPICS = ${TOPICS[*]}"
    echo "DEBUG: KAFKA_GROUPS = ${KAFKA_GROUPS[*]}"

    for ((i=0; i<${#TOPICS[@]}; i++)); do
        topic="${TOPICS[$i]}"
        group_id="${KAFKA_GROUPS[$i]}"

        # Debugging: Check the values
        echo "DEBUG: Assigning topic '$topic' to group '$group_id'"

        # Start a real consumer to register the group
        timeout 5s $KAFKA_BIN/kafka-console-consumer.sh \
            --bootstrap-server "$BOOTSTRAP_SERVER" \
            --topic "$topic" \
            --group "$group_id" \
            --from-beginning --max-messages 1 > /dev/null 2>&1

        echo "✅ Consumer group created: $group_id"
    done
}

# Execute Functions
create_topics
create_groups

# Print Summary
echo -e "\n🎉 Kafka Setup Completed!"
echo "📌 Topics Created: ${TOPICS[*]}"
echo "📌 Groups Created: ${KAFKA_GROUPS[*]}"
