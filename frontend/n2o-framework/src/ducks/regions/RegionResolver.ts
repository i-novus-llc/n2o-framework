// eslint-disable-next-line @typescript-eslint/no-extraneous-class
class RegionResolver {
    static transformedEntity(activeEntity: string) {
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
