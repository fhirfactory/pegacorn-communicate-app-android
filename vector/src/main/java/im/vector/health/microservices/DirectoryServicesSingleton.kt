package im.vector.health.microservices

import im.vector.health.microservices.Interfaces.IDirectoryServiceProvider

class DirectoryServicesSingleton {
    companion object {
        private var directoryServiceProvider: IDirectoryServiceProvider? = null
        fun Instance(): IDirectoryServiceProvider {
            if (directoryServiceProvider == null) directoryServiceProvider = DirectoryConnector()
            return directoryServiceProvider as IDirectoryServiceProvider
        }
    }

}