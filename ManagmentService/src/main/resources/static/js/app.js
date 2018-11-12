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
});

function addNewReplica(id, status='RUNNING') {
    let replica = `<tr id="row-${id}"><td>${id}</td><td name="status" class="${status.toLowerCase()}">${status}</td><td><a class="remove-replica">Remove</a></td></tr>`;
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
}