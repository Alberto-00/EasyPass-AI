<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <jsp:include page="/WEB-INF/Interface/Partials/Head.jsp">
        <jsp:param name="docenteStyles" value="docente,seatchart"/>

        <jsp:param name="title" value="Easy Pass | Docente"/>
    </jsp:include>

    <style>
        .content {
            display: flex;
            flex-direction: row;
            justify-content: center;
            margin-top: 40px;
        }

        .right {
            display: flex;
            flex-direction: column;
            margin-left: 80px;
        }

        #map-container {
            display: flex;
            align-items: center;
        }

        #legend-container {
            margin-top: 20px;
        }
    </style>
</head>
<body>
<div class="content">
    <div id="map-container"></div>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/Docente/seatchart.js"></script>

<script>
    const options = {
        // Reserved and disabled seats are indexed
        // from left to right by starting from 0.
        // Given the seatmap as a 2D array and an index [R, C]
        // the following values can obtained as follow:
        // I = columns * R + C
        map: {
            id: 'map-container',
            rows: 10,
            columns: 14,
            // e.g. Reserved Seat [Row: 1, Col: 2] = 7 * 1 + 2 = 9
            reserved: {
                //seats: [1, 2, 3, 5, 6, 7, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21],
                seats:[],
            },
            disabled: {
                seats: [7],
                //rows: [3],
                columns: [7]
            }
        },
        types: [
            { type: "reduced", backgroundColor: "#287233", price: 7.5, selected: [] }
        ],
    };

    const sc = new Seatchart(options);
</script>
</body>
</html>
