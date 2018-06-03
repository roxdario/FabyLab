package seatIn.latoServer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable{
	private static final long serialVersionUID = 1;

    private String name = null;
    private String description = null;
    private String code = null;
    private String visibility = null;
    private String file;
    private int year;
    private List<Node> children = new ArrayList<>();
    private Node parent = null;

    //solo per la radice
    //nome del corso + descrizione corso
    public Node(String name, String description, String code, int year) {
        this.name = name;
        this.description = description;
        this.code=code;
        this.year=year;
    }

    //sezione corso o sottosezione
    //ha tipo file=true per differenziarlo da file e cartella
    public Node(String name, String description, String code, String visibility) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.visibility = visibility;
        this.file="noFile";
    }

    //risorse file (1) o cartella (0)
    public Node(String name, String description, String code, String file, String visibility) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.visibility = visibility;
        //se file=1, se cartella=0;
        this.file = file;
    }

    public Node addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
        return child;
    }

    public void addChildren(List<Node> children) {
        children.forEach(each -> each.setParent(this));
        this.children.addAll(children);
    }

    public List<Node> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public void setData(String name) {
        this.name = name;
    }

    private void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCode() {
        return this.code;
    }

    public String getVisibility() {
        return this.visibility;
    }

    public String getFile() {
        return this.file;
    }
    public int getYear() {return this.year;}

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public List allNodeChildren() {
        List<Node> allChildren = new ArrayList<Node>();
        //ottengo la lista di tutti i figli di primo livello del nodo
        for (Node child : this.getChildren()) {
            allChildren.add(this);
            allChildren.addAll(this.getChildren());
            child.allNodeChildren();
        }
        return allChildren;
    }

    public static void printTree(Node root) {
        System.out.println("root: " + root.getName() + root.getDescription());
        //figli di primo livello + sottosezioni + file
        int i=0;
        List<Node> l = root.getChildren();
        for (Node n : l) {
            i++;
                 if (n.getVisibility().equals("pubblica"))
                     System.out.println("  sezione: " + n.getName());
            if (n.getChildren().size() != 0) {
                //potrebbe avere file, cartelle o sottosezioni!
                List<Node> list = n.getChildren();
                for (Node nodo : list) {
                    if (nodo.getFile().equals("false"))
                    //e' una cartella senza sottosezioni!
                    {   i++;
                        System.out.println("     cartel:" + nodo.getName() );
                        List<Node> nodeFile = nodo.getChildren();
                        //print di tutti i file
                        for (Node files : nodeFile) {
                            System.out.println("      file:" + files.getName());
                            i++;
                        }
                    } if (nodo.getFile().equals("true"))
                        //e' un file senza sottosezioni, che ovviamente non puo' avere figli!!
                    {
                        //print di tutti i file
                            System.out.println("      file:" + nodo.getName());
                            i++;
                    }
                    else
                    //significa che e' una sottosezione
                    {
                        System.out.println("    subsection: " + nodo.getName());
                        i++;
                        List<Node> nodes = nodo.getChildren();
                        for (Node nodoo : nodes) {
                            if (nodoo.getFile().equals("false"))
                            //e' una cartella senza sottosezioni!
                            {   i++;
                                System.out.println("         cartel:" + nodoo.getName());
                                List<Node> nodeFil = nodoo.getChildren();
                                //print di tutti i file
                                for (Node files : nodeFil) {
                                    i++;
                                    System.out.println("            file:" + files.getName());
                                }
                            }
                            if (nodoo.getFile().equals("true"))
                            //e' un file senza cartella!
                            {
                                i++;
                                //print di tutti i file
                                    System.out.println("         file:" + nodoo.getName());
                                }
                            }

                        }
                    }

                }
            }
            System.out.println(i);

        }
        public List<String> findNode(String code){
        List<Node> n=this.getChildren();
        List<String> lic=new ArrayList<String>();
        for (Node node: n)
        {
            if (node.getCode()==code)
            {
                List<Node> found=node.getChildren();
                for (Node f:found)
                lic.add(f.getCode());

            }
            else
            {
               List<Node> secondList=node.getChildren();
               for (Node sln: secondList)
                   sln.findNode(code);
            }
        }
        return lic;
        }

        public int treeLength() {

            //figli di primo livello + sottosezioni + file
            int i = 0;
            List<Node> l = this.getChildren();
            for (Node n : l) {
                i++;
                System.out.println("  sezione: " + n.getName());
                if (n.getChildren().size() != 0) {
                    //potrebbe avere file, cartelle o sottosezioni!
                    List<Node> list = n.getChildren();
                    for (Node nodo : list) {
                        if (nodo.getFile().equals("false"))
                        //e' una cartella senza sottosezioni!
                        {
                            i++;
                            System.out.println("     cartel:" + nodo.getName());
                            List<Node> nodeFile = nodo.getChildren();
                            //print di tutti i file
                            for (Node files : nodeFile) {
                                System.out.println("      file:" + files.getName());
                                i++;
                            }
                        }
                        if (nodo.getFile().equals("true"))
                        //e' un file senza sottosezioni, che ovviamente non puo' avere figli!!
                        {
                            //print di tutti i file
                            System.out.println("      file:" + nodo.getName());
                            i++;
                        } else
                        //significa che e' una sottosezione
                        {
                            System.out.println("    subsection: " + nodo.getName());
                            i++;
                            List<Node> nodes = nodo.getChildren();
                            for (Node nodoo : nodes) {
                                if (nodoo.getFile().equals("false"))
                                //e' una cartella senza sottosezioni!
                                {
                                    i++;
                                    System.out.println("         cartel:" + nodoo.getName());
                                    List<Node> nodeFil = nodoo.getChildren();
                                    //print di tutti i file
                                    for (Node files : nodeFil) {
                                        i++;
                                        System.out.println("            file:" + files.getName());
                                    }
                                }
                                if (nodoo.getFile().equals("true"))
                                //e' un file senza cartella!
                                {
                                    i++;
                                    //print di tutti i file
                                    System.out.println("         file:" + nodoo.getName());
                                }
                            }

                        }
                    }

                }
            }
            System.out.println(i);


            return i;
        }

    }