import { compose } from 'redux'
import { FC } from 'react'

import { withFetchData } from './withFetchData'
import { withListContainer } from './withListContainer'

/**
 * Композер хоков {@Link withFetchData} & {@Link withListContainer}
 * @param WrappedComponent - оборачиваемый компонент
 */
const listContainer = (WrappedComponent: FC) => compose(
    withFetchData,
    withListContainer,
)(WrappedComponent)

export default listContainer
