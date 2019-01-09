% edit this if you want to only import part of the data. Include 1 in the
% vector if you want to ignore the graphData, 2 for agentTrance and 3 for
% the agentLog.
ignore = [];

% add paths
addpath(genpath('helperFiles'))
addpath(genpath('customFiles'))
% Get the foder
folderName = uigetdir;
% Get the files in the folder
files = dir(folderName);
% Get a logical vector that tells which is a directory
dirFlags = [files.isdir];
% Extract only those that are directories
subFolders = files(dirFlags);
% Remove the '.' and '..' directories
subFolders(1:2,:) = [];

% create strcuture
bulkData = struct;
% for each subfolder
for i = 1: length(subFolders)
    % import data
    [graphData, agentTrace, agentLog] = importAATOMDataFunction(strcat(folderName,filesep,subFolders(i).name), ignore);
    % assing data to structure
    bulkData(i).folderName = subFolders(i).name;
    bulkData(i).graphData = graphData;
    bulkData(i).agentTrace = agentTrace;
    bulkData(i).agentLog = agentLog;
end;
% clear vars
clear agentLog agentTrace dirFlags files folderName graphData i subFolders