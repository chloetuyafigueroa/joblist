	        	
var app=angular.module('firstAPP',[]);

	app.controller('myCtrl1',function($scope,$http,$q)
	{
		$scope.sendStatus="...";
		$scope.message="...";
	
	
		$scope.readSMS=function(){
			$scope.message="Processing..."
			$http.post('Main?action=read',[$scope.comPort]).then(function mySuccs(response2){
				//var sexy21 = JSON.parse(JSON.stringify(response2.data));
				console.log(response2.data);
				$scope.message=response2.data;
			});	
			
			
		}
		$scope.readSerial=function(){
			$scope.message="Processing..."
			$http.post('Main?action=readserial',[$scope.comPort]).then(function mySuccs(response2){
				//var sexy21 = JSON.parse(JSON.stringify(response2.data));
				console.log(response2.data);
				$scope.message=response2.data;
			});	
			
			
		}
		$scope.debug=function(){
			$scope.message="Processing..."
			$http.post('Main?action=debug',[$scope.comPort]).then(function mySuccs(response2){
				//var sexy21 = JSON.parse(JSON.stringify(response2.data));
				console.log(response2.data);
				$scope.message=response2.data;
			});	
			
			
		}
		$scope.deleteSMS=function(){
			$scope.message="Processing..."
			$http.post('Main?action=delete',[$scope.CMGL,0,$scope.comPort]).then(function mySuccs(response2){
				//var sexy21 = JSON.parse(JSON.stringify(response2.data));
				console.log(response2.data);
				$scope.message = response2.data;
			});	
			
			
		}
		$scope.sendSMSPDU=function(){
			$scope.sendStatus="Processing..."
			$http.post('Main?action=sendPDU',[$scope.Add,$scope.Msg,$scope.comPort]).then(function mySuccs(response2){
				//var sexy21 = JSON.parse(JSON.stringify(response2.data));
				console.log(response2.data);
				$scope.sendStatus = response2.data;
			});	
			
			
		}
		$scope.readSMSPDU=function(){
			$scope.message="Processing..."
			$http.post('Main?action=readPDU',[$scope.comPort]).then(function mySuccs(response2){
				//var sexy21 = JSON.parse(JSON.stringify(response2.data));
				console.log(response2.data);
				$scope.message=response2.data;
			});	
			
			
		}
		$scope.Open=function(){
			console.log($scope.comPort);
			$http.post('Main?action=open',[$scope.comPort]).then(function mySuccs(response2){
				//var sexy21 = JSON.parse(JSON.stringify(response2.data));
				$scope.message=response2.data;
			});	
			
			
		}
		$scope.Close=function(){
			$http.post('Main?action=close').then(function mySuccs(response2){
				//var sexy21 = JSON.parse(JSON.stringify(response2.data));
				console.log(response2.data);
				$scope.message=response2.data;
			});	
			
			
		}
		$scope.Restart=function(){
			$http.get('Main?action=restart').then(function mySuccs(response2){
				//var sexy21 = JSON.parse(JSON.stringify(response2.data));
				console.log(response2.data);
				$scope.message=response2.data;
			});	
			
			
		}
		$scope.Listen=function(){
			$http.post('Main?action=listen').then(function mySuccs(response2){
				//var sexy21 = JSON.parse(JSON.stringify(response2.data));
				console.log(response2.data);
				$scope.message=response2.data;
			});	
			
			
		}
		$scope.stopListen=function(){
			$http.post('Main?action=stoplisten').then(function mySuccs(response2){
				//var sexy21 = JSON.parse(JSON.stringify(response2.data));
				console.log(response2.data);
				$scope.message=response2.data;
			});	
			
			
		}
		const baseUrl = window.location.origin; // Gets the base URL of your app
		const socketUrl = `${baseUrl}/Joblist/notifications`;
		 var ws = new WebSocket(socketUrl);
			ws.onopen = function() {
			    console.log('WebSocket connection opened.');
			    ws.send('Hello, Server!');
			};
			ws.onmessage = function(event) {
			    console.log('Message from server:', event.data);
				$scope.message=event.data;
			};
			ws.onclose = function() {
			    console.log('WebSocket connection closed.');
			};

			$scope.sendSocket=function(message){
			if (ws.readyState === WebSocket.OPEN) {
            ws.send(message);
            console.log('Message sent:', message);
	        } else {
	            console.log('WebSocket is not open. Message not sent.');
	        }
		}
			
									
			
									
});
				
   	 	
    	           
				
			
					   

		              	
		             	        
		       