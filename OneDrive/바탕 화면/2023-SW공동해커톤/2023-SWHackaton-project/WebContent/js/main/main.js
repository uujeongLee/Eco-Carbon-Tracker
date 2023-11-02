window.onload = init;

//지도
var map;
var wmsSource;
var wmsLayer = null;
var layerName;


var vectorSource = new ol.source.Vector({
	   
});

var vectorLayer = new ol.layer.Vector({
    source: vectorSource
});


// 지도 표출

//클릭시 popup
var overlay;


const baseMap =  new ol.layer.Tile({ //  기본 지도 layer 정의
    name:'analyMap',
    title:'analyMap',
    layerId:'analyMap',
    visible:true,
    source : new ol.source.XYZ({
    		 url: 'http://xdworld.vworld.kr:8080/2d/Base/202002/{z}/{x}/{y}.png',  // 색깔 지도

    })
});

var colorMap = new ol.source.XYZ({ // 일반지도
    url:'http://xdworld.vworld.kr:8080/2d/Base/202002/{z}/{x}/{y}.png'
});

var satelliteMap = new ol.source.XYZ({ // 위성지도
    url: 'https://xdworld.vworld.kr/2d/Satellite/service/{z}/{x}/{y}.jpeg'
});

var grayMap = new ol.source.XYZ({ 
	url: 'https://api.vworld.kr/req/wmts/1.0.0/2AAC4DD9-4F6F-3844-A740-E2DB6BDC8CEF/gray/{z}/{y}/{x}.png' 
});


//select item
var sd;
var sgg;
var year;
var month;

function init(){

    const container = document.getElementById('popup');
    const content = document.getElementById('popup-content');
    const closer = document.getElementById('popup-closer');

    overlay = new ol.Overlay({
        element : container,
        autoPan : true,
        //offset: [10, -50],
        positioning: 'bottom',
        autoPanAnimation : {
            duration : 250,
        }
    });

    closer.onclick = function () {
        overlay.setPosition(undefined);
        closer.blur();
        return false;
    };

    //overviewmap 생성하기
    var overViewMapCtrl = new ol.control.OverviewMap({
        className: 'ol-overviewmap ol-custom-overviewmap',
        layers: [
            new ol.layer.Tile({
                source: new ol.source.XYZ({
                    'url': 'http://xdworld.vworld.kr:8080/2d/Base/202002/{z}/{x}/{y}.png',
                }),
            }),
        ],
        collapseLabel: '\u00BB',
        label: '\u00AB',
        collapsed: false,

    });

    map = new ol.Map({
        target : 'map',
        view : new ol.View({
            projection : 'EPSG:3857',
            center :new ol.geom.Point([126.988220, 37.550565]).transform('EPSG:4326', 'EPSG:3857').getCoordinates(),
            zoom : 7.6,
            minZoom : 7,
            maxZoom : 19,
        }),
        layers: [baseMap],
        overlays: [overlay],
        controls: ol.control.defaults().extend([overViewMapCtrl])
    });


    //경기전역 center
    map.getView().setCenter(new ol.geom.Point([126.988220, 37.550565]).transform('EPSG:4326', 'EPSG:3857').getCoordinates());
    map.getView().setZoom(10.5);

    
    //tool 함수
    sideChangeDimension();
    mapZoomControl();
    sideLayoutReduceSetting();
    
    map.on('click', function (e) {
        var coordinate = e.coordinate;

        var view = map.getView();

        //클릭한 feature의 속성얻기
        map.forEachFeatureAtPixel(e.pixel, function (feature, layer) {
                    let values = feature.getProperties();
                    
                    var data = {
                    		mgm_bld_pk:	values.id,
                    		year: year,
                    		month: month
                    };
                    
                	$.ajax({
                		url : '/ajax/searchBuildingInfo.do',
                		data : data,
                		dataType : 'json',
                		contentType : "application/json; charset=utf-8",
                		async : false,
                		type : 'GET',
                		success : function(data) {
                			
                            var popupInfo = '<p style="">' + data.boardList.address + '</p>';
                            popupInfo += '<div>전기 배출량: '+(data.boardList.ele)+'</div>';
                            popupInfo += '<div>가스 배출량:' + (data.boardList.gas)+'</div>';
                            popupInfo += '<div style="">합계: ' + (data.boardList.ele + data.boardList.gas).toFixed(2) +  " tonCO2eq" +'</div>';
                            popupInfo += '<div>다음달 예측 값: ' + (data.boardList.predict_data).toFixed(2) + "tonCO2eq" + '</div>'
                            content.innerHTML = popupInfo;
                			
                			overlay.setPosition(coordinate);
                		},
                		error : function(request, status, error) {
                			console.log("데이터 조회 오류\ncode:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
                		}
                	});
                    
                },
		        {
		            hitTolerance: 2,
		            layerFilter: function(layer) {
		                return layer === vectorLayer;
		            }
		        }
            ); 
    });
    
/*    $(".getSggListBySd").click(function(e, i){
    	
    	var data = {
    			sd: $(".getSggListBySd option:selected").val()
    		};
    	
    	$.ajax({
    		url : '/ajax/getSggListBySd.do',
    		data : data,
    		dataType : 'json',
    		contentType : "application/json; charset=utf-8",
    		async : false,
    		type : 'GET',
    		success : function(data) {
    			$("#sgg").empty();
    			$("#sgg").append('<option value="00">'+'시군구'+'</option>');
    			data.boardList.forEach(function(e, i){
    				
    				$("#sgg").append('<option value="'+e.sgg_cd+'">'+e.sgg_nm+'</option>');
    			});
    			
    		},
    		error : function(request, status, error) {
    			console.log("데이터 조회 오류\ncode:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
    		}
    	});
    	
    	
    });*/
}


//충청남도 빌딩검색
function searchBuilding(){

	sd = $(".getSggListBySd option:selected").val();
	sgg = $("#sgg option:selected").val();
	year = $("#year option:selected").val();
	month = $("#month option:selected").val();
	
	var data = {
		sd: sd,
		sgg: sgg,
		year: year,
		month: month
	};
	
	$.ajax({
		url : '/ajax/searchBuildingLoc.do',
		data : data,
		dataType : 'json',
		contentType : "application/json; charset=utf-8",
		async : false,
		type : 'GET',
		success : function(data) {
			
			//이전꺼 있으면 reset
			if(vectorLayer != null){
				map.removeLayer(vectorLayer);
				vectorSource.clear();
			}
			
			data.boardList.forEach(function(e, i){
				var codi = new ol.geom.Point([e.x, e.y]).transform('EPSG:4326', 'EPSG:3857').getCoordinates();

				//vectorSource.clear();
				
				var iconFeature = new ol.Feature({
				    geometry: new ol.geom.Point(codi),
				    name: e.address,
				    id: e.mgm_bld_pk,
				    population: 4000,
				    rainfall: 500
				});
				
				var emission = e.ele + e.gas;
				var imgsrc;
				var imgsize;
				if(emission < 0.25){
					imgsrc = '/img/main/green.svg';
					imgsize = 1
				} else if(emission > 0.25 && emission <= 0.5){
					imgsrc = '/img/main/lightgGreen.svg';
					imgsize = 1.2
				} else if(emission > 0.5 && emission <= 0.75){
					imgsrc = '/img/main/yellow.svg';
					imgsize = 1.4;
				} else if(emission > 0.75 && emission <= 1){
					imgsrc = '/img/main/orange.svg';
					imgsize = 1.6
				} else if(emission > 1){
					imgsrc = '/img/main/red.svg';
					imgsize = 1.8;
				}
				
				var iconStyle = new ol.style.Style({
				    image: new ol.style.Icon(({
				        anchor: [0.5, 46],
				        anchorXUnits: 'fraction',
				        anchorYUnits: 'pixels',
				        src: imgsrc,
				        scale: imgsize
				    }))
				});
				iconFeature.setStyle(iconStyle);
				vectorSource.addFeature(iconFeature);
			})
			
			map.addLayer(vectorLayer);
		},
		error : function(request, status, error) {
			console.log("데이터 조회 오류\ncode:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);
		}
	});
}






