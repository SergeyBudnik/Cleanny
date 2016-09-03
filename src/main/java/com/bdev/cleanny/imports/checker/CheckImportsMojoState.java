package com.bdev.cleanny.imports.checker;

enum CheckImportsMojoState {
    START {
        @Override
        public CheckImportsMojoState getCurrentState(String line) {
            if (line.startsWith("package")) {
                return ON_PACKAGE;
            } else if (line.startsWith("import")) {
                return ON_IMPORTS;
            } else {
                return FINISH;
            }
        }
    },

    ON_PACKAGE {
        @Override
        public CheckImportsMojoState getCurrentState(String line) {
            if (line.startsWith("import")) {
                return ON_IMPORTS;
            } else {
                return FINISH;
            }
        }
    },

    ON_IMPORTS {
        @Override
        public CheckImportsMojoState getCurrentState(String line) {
            if (line.startsWith("import")) {
                return ON_IMPORTS;
            }

            return FINISH;
        }
    },

    FINISH {
        @Override
        public CheckImportsMojoState getCurrentState(String line) {
            return FINISH;
        }
    };

    public abstract CheckImportsMojoState getCurrentState(String line);
}
