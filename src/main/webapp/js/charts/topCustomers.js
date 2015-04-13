$(function () {

    var customerStatistics = $("#j_idt28\\:topCustomersData").val();

    var json = jQuery.parseJSON( customerStatistics );

    var categories = [];
    var values = [];

    for(var i = 0; i < 10; i++) {
        categories.push(json[i].name);
    }

    for(var i = 0; i < 10; i++) {
        values.push(json[i].moneySpent);
    }

    $('#topCustomers').highcharts({
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Best Customers'
        },
        subtitle: {
            text: 'TOP 10'
        },
        xAxis: {
            categories: categories,
            title: {
                text: null
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: '',
                align: 'high'
            },
            labels: {
                overflow: 'justify'
            }
        },

        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -40,
            y: 100,
            floating: true,
            borderWidth: 1,
            backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
            shadow: true
        },
        credits: {
            enabled: false
        },
        series: [{
            name: 'EUR',
            data: values
        },]
    });
});


function sleep(miliseconds) {
    var currentTime = new Date().getTime();

    while (currentTime + miliseconds >= new Date().getTime()) {
    }
}