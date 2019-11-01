box.cfg{listen=3301}
space = box.schema.space.create('test',{id=666})
space:format({{name = 'id', type = 'unsigned'}, {name = 'contract_id', type = 'unsigned'}, {name = 'contract_name', type = 'string'}, {name = 'user_id', type = 'unsigned'}, {name = 'expiration_timestamp', type = 'unsigned'}})
space:create_index('primary', {type = 'hash', parts = {'id'}})
box.schema.user.grant('guest','read,write','space','test')
box.schema.user.grant('guest','read','space','_space')
