package xyz.demj.libs.filechooser;

import java.io.File;

interface FileShowStrategy {
    boolean show(File pFile);

    static class NoneShowStrategy implements FileShowStrategy {
        protected final FileShowStrategy mShowStrategy;

        NoneShowStrategy(FileShowStrategy pFileShowStrategy) {
            mShowStrategy = pFileShowStrategy;
        }

        @Override
        public boolean show(File pFile) {
            return mShowStrategy != null && mShowStrategy.show(pFile);
        }
    }

    static class ShowFolerStrategy extends NoneShowStrategy {
        ShowFolerStrategy(FileShowStrategy pFileShowStrategy) {
            super(pFileShowStrategy);
        }

        @Override
        public boolean show(File pFile) {
            return pFile.isDirectory() || super.show(pFile);
        }
    }

    static class ShowFileStrategy extends NoneShowStrategy {

        ShowFileStrategy(FileShowStrategy pFileShowStrategy) {
            super(pFileShowStrategy);
        }

        @Override
        public boolean show(File pFile) {
            return pFile.isFile() || super.show(pFile);
        }
    }
}