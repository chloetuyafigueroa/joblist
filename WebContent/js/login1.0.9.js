	        	
   	 		var app=angular.module('firstAPP',[]);
	   	 	
				app.controller('myCtrl1',function($scope)
				{
					
					$scope.mTest="su";
					
					$scope.$watch('uname',function(){
						$scope.mTest=$scope.uname;
						localStorage.setItem("str_name",$scope.uname)
						
					});
					
						
			});
				             	        
		       