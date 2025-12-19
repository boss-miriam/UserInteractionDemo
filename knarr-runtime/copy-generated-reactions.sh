#!/bin/bash

# ============================================================================
# Copy Generated Reactions from Amalthea-acset to amalthea-acset-integration
# ============================================================================
# This script copies the generated reactions and routines from the external
# Amalthea-acset project to the internal amalthea-acset-integration module.
#
# Usage:
#   ./copy-generated-reactions.sh
#   ./copy-generated-reactions.sh --external-path /path/to/Amalthea-acset
#
# ============================================================================

set -e

# Default paths
EXTERNAL_PATH="/home/anne/CocoPath/Amalthea-acset"
INTERNAL_PATH="/home/anne/CocoPath/CocoPath/amalthea-acset-integration"

# Parse arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --external-path)
            EXTERNAL_PATH="$2"
            shift 2
            ;;
        --internal-path)
            INTERNAL_PATH="$2"
            shift 2
            ;;
        -h|--help)
            echo "Usage: $0 [OPTIONS]"
            echo ""
            echo "Options:"
            echo "  --external-path PATH  Path to external Amalthea-acset (default: $EXTERNAL_PATH)"
            echo "  --internal-path PATH  Path to internal amalthea-acset-integration (default: $INTERNAL_PATH)"
            echo "  -h, --help           Show this help message"
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            echo "Use -h for help"
            exit 1
            ;;
    esac
done

echo "================================================================================"
echo "Copy Generated Reactions and Routines"
echo "================================================================================"
echo ""
echo "From: $EXTERNAL_PATH"
echo "To:   $INTERNAL_PATH"
echo ""

# Verify external path exists
if [ ! -d "$EXTERNAL_PATH" ]; then
    echo "ERROR: External Amalthea-acset not found at: $EXTERNAL_PATH"
    echo "Please specify correct path with --external-path"
    exit 1
fi

# Verify internal path exists
if [ ! -d "$INTERNAL_PATH" ]; then
    echo "ERROR: Internal amalthea-acset-integration not found at: $INTERNAL_PATH"
    echo "Please specify correct path with --internal-path"
    exit 1
fi

# Check if generated sources exist
GENERATED_DIR="$EXTERNAL_PATH/consistency/target/generated-sources/reactions"
if [ ! -d "$GENERATED_DIR" ]; then
    echo "ERROR: Generated sources not found at: $GENERATED_DIR"
    echo ""
    echo "You may need to build the external project first:"
    echo "  cd $EXTERNAL_PATH"
    echo "  mvn clean generate-sources"
    exit 1
fi

# Create target directory if it doesn't exist
TARGET_DIR="$INTERNAL_PATH/consistency/src/main/java/mir"
if [ ! -d "$TARGET_DIR" ]; then
    echo "Creating target directory: $TARGET_DIR"
    mkdir -p "$TARGET_DIR"
fi

# Backup existing files if they exist
if [ -d "$TARGET_DIR/reactions" ] || [ -d "$TARGET_DIR/routines" ]; then
    BACKUP_DIR="$INTERNAL_PATH/consistency/src/main/java/mir.backup.$(date +%Y%m%d_%H%M%S)"
    echo "Backing up existing files to: $BACKUP_DIR"
    mkdir -p "$BACKUP_DIR"

    if [ -d "$TARGET_DIR/reactions" ]; then
        cp -r "$TARGET_DIR/reactions" "$BACKUP_DIR/"
    fi

    if [ -d "$TARGET_DIR/routines" ]; then
        cp -r "$TARGET_DIR/routines" "$BACKUP_DIR/"
    fi
    echo "Backup completed."
    echo ""
fi

# Copy reactions
if [ -d "$GENERATED_DIR/mir/reactions" ]; then
    echo "Copying reactions..."
    rm -rf "$TARGET_DIR/reactions" 2>/dev/null || true
    cp -r "$GENERATED_DIR/mir/reactions" "$TARGET_DIR/"
    REACTION_COUNT=$(find "$TARGET_DIR/reactions" -name "*.java" | wc -l)
    echo "  Copied $REACTION_COUNT reaction files"
else
    echo "WARNING: No reactions found to copy"
fi

# Copy routines
if [ -d "$GENERATED_DIR/mir/routines" ]; then
    echo "Copying routines..."
    rm -rf "$TARGET_DIR/routines" 2>/dev/null || true
    cp -r "$GENERATED_DIR/mir/routines" "$TARGET_DIR/"
    ROUTINE_COUNT=$(find "$TARGET_DIR/routines" -name "*.java" | wc -l)
    echo "  Copied $ROUTINE_COUNT routine files"
else
    echo "WARNING: No routines found to copy"
fi

echo ""
echo "================================================================================"
echo "Copy completed successfully!"
echo "================================================================================"
echo ""
echo "Next steps:"
echo "1. Build the internal project:"
echo "   cd $INTERNAL_PATH"
echo "   mvn clean compile"
echo ""
echo "2. Run the instrumented tests:"
echo "   cd /home/anne/CocoPath/CocoPath/knarr-runtime"
echo "   ./run-instrumented-with-option-flags.sh -i"
echo ""