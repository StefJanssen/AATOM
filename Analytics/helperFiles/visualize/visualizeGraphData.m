function [] = visualizeGraphData(graphData)
%% plot agent traces
for i = 1:size(graphData,1)
    currentData = graphData(i,:);
    figure('Name',currentData{2}{1})
    plot(currentData{1}(:,1:2:end),currentData{1}(:,2:2:end));
    xlabel('Time (s)');
    ylabel(currentData{2}{2});
    legend(currentData{2}{3});
end
end