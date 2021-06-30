class RegionResolver {
    static transformedEntity(activeEntity) {
        switch (activeEntity) {
            case 'true':
                return true
            case 'false':
                return false
            default:
                return activeEntity
        }
    }
}

export default RegionResolver
