package org.crank.archetype;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class ArchetypeUtils {
    interface FileCollectable {
        public void takeThisFile(File file);
    }
    static class FileCollector implements FileCollectable {
        List <File> files = new ArrayList <File> ();
        public void takeThisFile( File file ) {
            files.add( file );
        }
    }
    private static String TEMPLATE =
                                        "<archetype> \n" +
                                        "     <id>%s</id>\n" +
                                        "     <sources>\n" +
                                        "%s" +
                                        "     </sources>\n" +
                                        "     <resources>\n" +
                                        "           <source filtered='true'>pom.xml</source>\n" +
                                        "%s" +
                                        "%s" +
                                        "%s" +                                        
                                        "     </resources>\n" +
                                        "     <testSources>\n" +
                                        "%s" +
                                        "     </testSources>\n" +
                                        "     <testResources>\n" +
                                        "%s" +
                                        "     </testResources>\n" +
                                        "</archetype>";
    private static String SOURCE_TEMPLATE = 
                                        "           <source filtered='false'>%s</source>\n";

    public static String generateXML( String sRoot, String id ) throws Exception {
        File root = new File(sRoot);
        File sourcesFile = new File(sRoot, "src/main/java");
        File resourcesFile = new File(sRoot, "src/main/resources");
        File testSourcesFile = new File(sRoot, "src/test/java");
        File testResourcesFile = new File(sRoot, "src/test/resources");
        File webResourcesFile = new File(sRoot, "src/main/webapp");


        String sources = genResourceEntry(root, sourcesFile, true);
        String resources = genResourceEntry(root, resourcesFile, true);
        String testSources = genResourceEntry(root, testSourcesFile, true);
        String testResources = genResourceEntry(root, testResourcesFile, true);
        String webResources = genResourceEntry(root, webResourcesFile, true);
        String rootResources = genResourceEntry( root, root, false );
        return (new Formatter()).format(TEMPLATE, id, sources, resources, rootResources, webResources, testSources, testResources).toString();
    }
    
    private static String genResourceEntry( File root, File sourcesFile, boolean recurse ) throws Exception{
        StringBuilder builder = new StringBuilder();
        FileCollector collectable = new FileCollector();
        if (recurse) {
            walkFiles(sourcesFile, collectable);
        } else {
            File[] listFiles = root.listFiles( new FilenameFilter() {

                public boolean accept( File dir, String sfile ) {
                    if (sfile.startsWith( "." )) {
                        return false;
                    }
                    File file = new File(dir, sfile);
                    return file.isFile();
                }
            });
            for (File file : listFiles) {
                collectable.takeThisFile( file );
            }
        }
        List<String> sourcesList = filesToResources(root, collectable.files);
        for (String source : sourcesList) {
            builder.append( (new Formatter()).format(SOURCE_TEMPLATE, source).toString()  );
        }
        return builder.toString();
    }

    private static List<String> filesToResources(File root, List<File> files ) {
        List<String> list = new ArrayList<String>();
        for (File file : files) {
            list.add( file.toString().substring( root.toString().length()+1 ).replace( '\\', '/' ));
        }
        return list;
    }

    public static void walkFiles(File root, FileCollectable collectable) throws Exception {
        File[] files = root.listFiles();
        if (files==null) {
            return;
        }
        for (File file: files) {
            if (file.isDirectory()) {
                walkFiles( file, collectable );
            } else if (file.isFile()) {
                if (!file.toString().contains(".svn")) {
                    collectable.takeThisFile( file );
                }
            }
        }
    }
    static class Args {
        String outputDir=null;
        String inputDir=null;
        String id=null;
        boolean bad;
    }

    private static Args processArgs( String ... args ) throws Exception{
        Args theArgs = new Args();

        for (String arg : args) {
            if (arg.startsWith( "-o=") ){
                theArgs.outputDir = arg.substring( 3 );
            } else if (arg.startsWith( "-d=")) {
                theArgs.inputDir = arg.substring( 3 );
            } else if (arg.startsWith( "-i" )){
                theArgs.id = arg.substring( 3 );
            }
        }
        if (theArgs.id == null || theArgs.inputDir == null || theArgs.outputDir == null) {
            System.out.printf("Proper usage is as follows:\n" +
                    "java org.crank.archetype.ArchetypeUtils -i=<Id> -d=<Input Dir> -o=outputDir\n" +
                    "Input Dir = %s Output Dir = %s Id = %s", theArgs.inputDir, theArgs.outputDir, theArgs.id);
            theArgs.bad = true;
        } else {
            System.out.printf("Input Dir = %s Output Dir = %s Id = %s", theArgs.inputDir, theArgs.outputDir, theArgs.id);
            theArgs.bad = false;
        }
        return theArgs;
    }
    
    public static void main (String... args) throws Exception {
        Args theArgs = processArgs( args );
        if (/* good arguments. */ theArgs.bad == false) {
            processDir(theArgs);
        }
    }

    private static void processDir( Args theArgs ) throws Exception {
        String fileData = generateXML( theArgs.inputDir, theArgs.id );
        File outDir = new File(theArgs.outputDir);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        File outputFile = new File(outDir, "archetype.xml");
        PrintWriter writer = new PrintWriter(new FileOutputStream(outputFile));
        try {
            writer.print( fileData );
        } finally {
            writer.close();
        }
    }
}
