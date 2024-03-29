package org.interprete.tool;


import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

// provalmente o autor do livro escreveu esse scritp pq não existiam records na epoca
public class GenerateAst {

    //    ta funcionando até então, só que preico por o caminho na mao em outputDir
//    vou melhorar depois ou talvez ele mostre isso ainda
    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            System.err.println("Usage: generate_ast <output directory>");
//            System.exit(64);
//        }
        String outputDir = args[0];
//        String outputDir = "./src/main/java/org/interprete/lox";

        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expr right"
        ));
    }

    private static void defineAst(
            String outputDir,
            String baseName,
            List<String> types) throws Exception {

        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");


        writer.println("package org.interprete.lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        defineVisitor(writer, baseName, types);


//        incricao da arvore
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();

            defineType(writer, baseName, className, fields);
        }

        // The base accept() method.
        writer.println();
        writer.println("  abstract <R> R accept(Visitor<R> visitor);");


        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(
            PrintWriter writer, String baseName, List<String> types) {

        writer.println("  interface Visitor<R> {");


        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("    R visit" + typeName + baseName + "(" +
                    typeName + " " + baseName.toLowerCase() + ");");
        }
        writer.println("  }");
    }

    private static void defineType(PrintWriter writer, String baseName,
                                   String className, String fieldList) {


        writer.println("  static class " + className + " extends " +
                baseName + " {");

//        construtor da class
        writer.println("    " + className + "(" + fieldList + ") {");


//        guardar lista de campos
        String[] fields = fieldList.split(",");
        for (String field : fields) {
            String name = field.trim().split(" ")[1];
            writer.println("      this." + name + " = " + name + ";");
        }

        writer.println("    }");

        //        padrao visitor
        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor<R> visitor) {");
        writer.println("      return visitor.visit" +
                className + baseName + "(this);");
        writer.println("    }");

        // cada variavel
        writer.println();
        for (String field : fields) {
            writer.println("    final " + field + ";");
        }


        writer.println("  }");
    }


}