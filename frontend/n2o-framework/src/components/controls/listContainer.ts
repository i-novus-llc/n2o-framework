import { compose } from 'redux'
import { ComponentType } from 'react'

import { withFetchData } from './withFetchData'
import { withListContainer } from './withListContainer'

/**
 * Композер хоков {@Link withFetchData} & {@Link withListContainer}
 * @param WrappedComponent - оборачиваемый компонент
 */
export const listContainer = <P>(WrappedComponent: ComponentType<P>) => compose(
    withFetchData,
    withListContainer,
)(WrappedComponent)

export default listContainer
