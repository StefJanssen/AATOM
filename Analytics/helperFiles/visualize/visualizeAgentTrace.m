function [] = visualizeAgentTrace(agentTrace)
%% plot agent traces
figure('Name','Traces of agents in simulation')
agentMatrix = cell2mat(agentTrace(:,2:4));
agentNumbers = unique(cell2mat(agentTrace(:,2)));

for i = 1 : length(agentNumbers)
    indices = find(agentMatrix(:,1) == agentNumbers(i));
    plot(agentMatrix(indices,2), -agentMatrix(indices,3));
    hold on
end
end