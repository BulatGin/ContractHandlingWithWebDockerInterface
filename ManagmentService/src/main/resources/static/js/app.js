let stompClient = null;

$(document).ready(function () {
    $.ajax({
        method: 'GET',
        url: '/api/replica',
        success: function (data) {
            data.forEach(replica => addNewReplica(replica.containerId, replica.status));
        }
    });

    $('#add-replica').click(function (e) {
        e.preventDefault();
        $.ajax({
            method: 'POST',
            url: '/api/replica',
            success: function (data) {
                addNewReplica(data);
            }
        });
    });

    connectToWSServer();
});

function addNewReplica(id, status='RUNNING', cpu=0.0, memory=0) {
    let replica = `<tr id="row-${id}">
                     <td>${id}</td>
                     <td name="status" class="${status.toLowerCase()}">${status}</td>
                     <td name="cpu">${cpu}%</td>
                     <td name="memory">${memory}Mb</tdname>
                     <td><a class="remove-replica">Remove</a></td>
                   </tr>`;
    $('#replicas-table-body').append(replica);

    $('.remove-replica').click(function (e) {
        e.preventDefault();

        $.ajax({
            method: 'DELETE',
            url: '/api/replica',
            data: id,
            contentType: 'text/plain; charset=UTF-8',
            success: function (data) {
                let elem = document.querySelector(`#row-${id} td[name="status"]`);
                elem.innerHTML = 'STOPPED';
                elem.className = 'stopped';
            },
            error: function (r) {
                console.log(r);
            }
        });
    });
};

function connectToWSServer() {
    let socket = new SockJS('/connect');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        subscribeToReplicasUpdates();
    });
};

function subscribeToReplicasUpdates() {
    stompClient.subscribe('/topic/replicasListUpdate', function (msg) {
        $('#replicas-table-body').empty();
        let replicas = JSON.parse(msg.body);
        replicas.forEach(r => addNewReplica(r.containerId,
            r.status,
            Math.round(r.cpu * 100)/100,
            Math.round(r.memoryUsage/1024/1024 * 100) / 100));
    });
    console.log('Subscribed to /topic/replicasListUpdate');
};