package info.manhhdtop.cloud.common.messaging.constants;

public interface RabbitMQConstant {
    interface User {
        String EXCHANGE = "user.exchange";
        
        String SYNC_QUEUE = "user.sync.queue";
        String CREATED_QUEUE = "user.created.queue";
        String UPDATED_QUEUE = "user.updated.queue";
        String LOCKED_QUEUE = "user.locked.queue";
        String UNLOCKED_QUEUE = "user.unlocked.queue";
        String DELETED_QUEUE = "user.deleted.queue";
        String STATUS_CHANGED_QUEUE = "user.status.changed.queue";
        
        String SYNC_ROUTING_KEY = "user.sync";
        String CREATED_ROUTING_KEY = "user.created";
        String UPDATED_ROUTING_KEY = "user.updated";
        String LOCKED_ROUTING_KEY = "user.locked";
        String UNLOCKED_ROUTING_KEY = "user.unlocked";
        String DELETED_ROUTING_KEY = "user.deleted";
        String STATUS_CHANGED_ROUTING_KEY = "user.status.changed";
    }

    interface Role {
        String EXCHANGE = "auth.exchange";
        
        String CREATE_REQUEST_QUEUE = "role.create.request.queue";
        String CREATED_QUEUE = "role.created.queue";
        String UPDATED_QUEUE = "role.updated.queue";
        String DELETED_QUEUE = "role.deleted.queue";
        
        String CREATE_REQUEST_ROUTING_KEY = "role.create.request";
        String CREATED_ROUTING_KEY = "role.created";
        String UPDATED_ROUTING_KEY = "role.updated";
        String DELETED_ROUTING_KEY = "role.deleted";
    }

    interface Permission {
        String EXCHANGE = "auth.exchange";
        
        String CREATE_REQUEST_QUEUE = "permission.create.request.queue";
        String CREATED_QUEUE = "permission.created.queue";
        String UPDATED_QUEUE = "permission.updated.queue";
        String DELETED_QUEUE = "permission.deleted.queue";
        
        String CREATE_REQUEST_ROUTING_KEY = "permission.create.request";
        String CREATED_ROUTING_KEY = "permission.created";
        String UPDATED_ROUTING_KEY = "permission.updated";
        String DELETED_ROUTING_KEY = "permission.deleted";
    }
}

