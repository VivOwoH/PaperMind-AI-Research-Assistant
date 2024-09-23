package com.project.main.service;

import com.project.main.entity.Graph;
import com.project.main.entity.AppResponse;
import com.project.main.repository.AppResponseRepo;
import com.project.main.repository.GraphRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GraphService {
    private final AppResponseRepo appResponseRepo;
    private final GraphRepo graphRepo;

    public GraphService(AppResponseRepo appResponseRepo, GraphRepo graphRepo) {
        this.appResponseRepo = appResponseRepo;
        this.graphRepo = graphRepo;
    }

    // retrieves all Graph entities from the repo
    public List<Graph> getAllGraphs() {
        return this.graphRepo.findAll();
    }

    // retrieves one Graph entity from the repo based on id
    public Graph getGraphById(Integer responseId) {
        Optional<Graph> optionalGraph = this.graphRepo.findById(responseId);
        if (optionalGraph.isPresent()) {
            return optionalGraph.get();
        }

        System.out.printf("Graph with id: %d doesn't exist", responseId);
        return null;
    }

    // retrieves one AppResponse entity from the repo based on Graph entity
    public AppResponse getAppResponseByGraphId(Integer responseId) {
        Optional<Graph> optionalGraph = this.graphRepo.findById(responseId);
        
        if (optionalGraph.isPresent()) {
            Graph graph = optionalGraph.get();
            return graph.getAppResponse();
        }

        System.out.printf("Graph with id: %d doesn't exist", responseId);
        return null;
    }

    // saves an Graph entity to the repo
    public Graph saveGraph(Graph graph) {
        // check if app response exists from the controller Graph parameter
        if (graph.getAppResponse() != null) {
            AppResponse appResponse = graph.getAppResponse();

            // add app response entity to graph
            if (appResponse.getId() != null) {
                Optional<AppResponse> appResponseOptional = appResponseRepo.findById(appResponse.getId());
                if (appResponseOptional.isPresent()) {
                    graph.setAppResponse(appResponseOptional.get());
                } else {
                    throw new IllegalArgumentException("AppResponse with the given ID does not exist");
                }
            }
        } else {
            // app response is not provided or doesn't have an ID
            throw new IllegalArgumentException("AppResponse ID must be provided");
        }

        Graph savedGraph = this.graphRepo.save(graph);
        System.out.printf("Graph with id: %d saved successfully", savedGraph.getId());
        return savedGraph;
    }

    public Graph updateGraph(Integer id, Graph newGraphDetails) {
        Optional<Graph> optionalGraph = this.graphRepo.findById(id);

        if (optionalGraph.isPresent()) {
            Graph existingGraph = optionalGraph.get();

            if (newGraphDetails.getId().equals(existingGraph.getId())) {
                existingGraph.setGraphType(newGraphDetails.getGraphType()); 
                existingGraph.setAppResponse(newGraphDetails.getAppResponse()); 

                // Save the updated entity
                Graph updatedGraph = this.graphRepo.save(existingGraph);

                System.out.printf("Graph with id: %d updated successfully", updatedGraph.getId());
                return updatedGraph;
            }
        } 

        System.out.printf("Graph with id: %d doesn't exist", id);
        return null;
    }


    // deletes an Graph from the repo based on id
    public void deleteGraphById(Integer id) {
        this.graphRepo.deleteById(id);
    }
}