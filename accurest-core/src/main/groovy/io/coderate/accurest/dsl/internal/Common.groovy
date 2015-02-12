package io.coderate.accurest.dsl.internal

import groovy.transform.PackageScope
import groovy.transform.TypeChecked

/**
 * @TypeChecked instead of @CompileStatic due to usage of double dispatch.
 * Double dispatch doesn't work if you're using @CompileStatic
 */
@TypeChecked
@PackageScope
class Common {

    Map<String, DslProperty> convertObjectsToDslProperties(Map<String, Object> body) {
        return body.collectEntries {
            Map.Entry<String, Object> entry ->
                [(entry.key): toDslProperty(entry.value)]
        } as Map<String, DslProperty>
    }

    DslProperty toDslProperty(Object property) {
        return new DslProperty(property)
    }

    DslProperty toDslProperty(DslProperty property) {
        return property
    }

    DslProperty value(ClientDslProperty client, ServerDslProperty server) {
        return new DslProperty(client.clientValue, server.serverValue)
    }

    DslProperty value(ServerDslProperty server, ClientDslProperty client) {
        return new DslProperty(client.clientValue, server.serverValue)
    }

    DslProperty $(ClientDslProperty client, ServerDslProperty server) {
        return value(client, server)
    }

    DslProperty $(ServerDslProperty server, ClientDslProperty client) {
        return value(client, server)
    }

    ClientDslProperty client(Object clientValue) {
        return new ClientDslProperty(clientValue)
    }

    ServerDslProperty server(Object serverValue) {
        return new ServerDslProperty(serverValue)
    }

}
