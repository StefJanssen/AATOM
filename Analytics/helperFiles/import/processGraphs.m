function [processedGraphs] = processGraphs( graphData )
graphs = strfind(graphData(:,1), '_graph_');
graphs = find(not(cellfun('isempty', graphs)));

lines = strfind(graphData(:,1), '_line_');
lines = find(not(cellfun('isempty', lines)));
lines = [lines; length(graphData(:,1))];

processedGraphs = cell(length(graphs),2);

for i = 1 : length(graphs)
    minLineIndex = find(lines == graphs(i)+1);
    maxLineIndex = -1;
    if i ~= length(graphs)
        maxLineIndex = find(lines == graphs(i+1)+1);
    else
        maxLineIndex = length(lines);
    end;
    graphName = char(graphData(graphs(i),1));
    graphName = graphName(8:end);
    indexOfUnderscore = strfind(graphName,'_');
    yLabel = graphName(1:indexOfUnderscore-1);
    graphName = graphName(indexOfUnderscore+1:end);
    
    myData = [];
    for j = minLineIndex : maxLineIndex-1
        addFactor = 0;
        if(j == maxLineIndex - 1)
            addFactor = 1;
            if(i == length(graphs))
                addFactor = -1;
            end;
            
        end;
        
        xData = str2double(graphData(lines(j)+1:lines(j+1)-1-addFactor,1));
        yData = cell2mat(graphData(lines(j)+1:lines(j+1)-1-addFactor,2));
        myData = [myData, xData, yData];
    end;
    
    lineNames = graphData(lines(minLineIndex:maxLineIndex-1));
    for j = 1 : length(lineNames)
        lineNames{j} = lineNames{j}(7:end);
    end;
    
    graphInformation = cell(3,1);
    graphInformation{1} = graphName;
    graphInformation{2} = yLabel;
    graphInformation{3} = lineNames;
    processedGraphs{i,1} = myData;
    processedGraphs{i,2} = graphInformation;
end;
end

