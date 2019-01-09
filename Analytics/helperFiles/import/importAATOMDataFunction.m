function [graphData, agentTrace, agentLog] = importAATOMDataFunction(path, ignore)
% importAATOMDataFunction  Imports data for a specific simulation. The path
% is the path to a folder that contains log files.
%   [graphData, agentTrace, agentLog] = importAATOMDataFunction(path) 
%        Imports all data of a simulation.
%   [graphData, agentTrace, agentLog] = importAATOMDataFunction(path, [3]) 
%        Imports all data of a simulation apart from the custom agent log.

%% Set file names
graphFileName = 'trackedParameters.txt';
agentTraceFileName = 'agentTrace.txt';
agentLogFileName = 'agentLog.txt';

%% Create empty data
graphData=[];
agentTrace =[];
agentLog = [];

%% Import data files
if(~any(ignore==1))
    graphData = importGraphs(strcat(path,filesep,graphFileName));
    graphData = processGraphs(graphData);
end;
if(~any(ignore==2))
    agentTrace = importAgentTrace(strcat(path,filesep,agentTraceFileName));
end;
if(~any(ignore==3))
    agentLog = importAgentLog(strcat(path,filesep,agentLogFileName));
end;
end