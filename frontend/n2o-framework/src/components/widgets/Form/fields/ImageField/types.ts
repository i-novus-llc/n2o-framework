import type { Props as ImageProps } from '@i-novus/n2o-components/lib/display/Image/Image'

import type { ImageStatusesType } from '../../../Table/cells/ImageCell/types'
import { ModelPrefix } from '../../../../../core/datasource/const'
import { type Props as ActionWrapperProps } from '../../../../buttons/StandardButton/ActionWrapper'

type Extension = ImageProps & ActionWrapperProps

export interface Props extends Extension {
    model: Record<string, unknown>
    data: string
    statuses: ImageStatusesType['statuses']
    modelPrefix?: ModelPrefix
    form?: string
}
