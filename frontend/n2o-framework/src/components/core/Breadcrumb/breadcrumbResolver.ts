import propsResolver from '../../../utils/propsResolver'

import { type Breadcrumb } from './const'

export const breadcrumbResolver = (
    model: Record<string, unknown> | Array<Record<string, unknown>>,
    breadcrumb: Breadcrumb,
): Breadcrumb => breadcrumb.map(({ label, ...rest }) => ({ label: propsResolver(label, model), ...rest }))
