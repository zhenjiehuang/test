package lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.hm.apollo.module.recognition.model.NodeSynonym;

public class Test {

    public static void main(String[] args) {
        NodeSynonym n = new NodeSynonym();

        List<NodeSynonym> ns = new ArrayList<NodeSynonym>();
        ns.add(n);

        ns.stream().collect(Collectors.groupingBy(NodeSynonym::getSynonymWord,
                Collectors.mapping(nn -> {
                    return "";
                }, Collectors.toList())));

        // Observable o = new Observable();
    }
}
