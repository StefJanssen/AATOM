% edit this if you want to only import part of the data. Include 1 in the
% vector if you want to ignore the graphData, 2 for agentTrance and 3 for
% the agentLog.
ignore = [];

% add paths
addpath(genpath('helperFiles'))
addpath(genpath('customFiles'))
% import data
[graphData, agentTrace, agentLog] = importAATOMDataFunction(uigetdir, ignore);