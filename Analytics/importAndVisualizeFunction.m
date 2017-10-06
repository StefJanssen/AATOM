function [graphData, agentTrace] = importAndVisualizeFunction(path, ignore, vis)
%% Import data
logFileName = 'agentTrace.txt';
graphFileName = 'trackedParameters.txt';
graphData=[]; 
agentTrace =[];

if(~any(ignore==1))
graphData = importGraphs(strcat(path,filesep,graphFileName));
end;
if(~any(ignore==2))
agentTrace = importLogfile(strcat(path,filesep,logFileName));
end;

%% Visualize and stats
if(nargin == 3)
    graphData = processGraphs(graphData, vis);
    if(~any(ignore==2))
        generateTracePlot(agentTrace);
    end;
end;
end