import {
    REGISTER_REGION,
    SET_ACTIVE_REGION_ENTITY,
    MAP_URL,
} from '../constants/regions'

import { registerRegion, setActiveEntity, mapUrl } from './regions'

const testWidgetId = 'someWidgetId'
const testRegionId = 'someRegionId'
const testInitProps = {
    regionId: testRegionId,
    some: 'value',
    isInit: true,
    list: [],
}

describe('тесты regions actions', () => {
    it('registerRegion', () => {
        const action = registerRegion(testRegionId, testInitProps)
        expect(action.type).toBe(REGISTER_REGION)
        expect(action.payload).toEqual({
            regionId: testRegionId,
            initProps: testInitProps,
        })
    })

    it('setActiveEntity', () => {
        const action = setActiveEntity(testRegionId, testWidgetId)
        expect(action.type).toBe(SET_ACTIVE_REGION_ENTITY)
        expect(action.payload).toEqual({
            regionId: testRegionId,
            activeEntity: testWidgetId,
        })
    })

    it('mapUrl', () => {
        expect(mapUrl().type).toBe(MAP_URL)
    })
})
