import propsResolver from '../../../utils/propsResolver'

import { breadcrumb } from './const'

export const breadcrumbResolver = (
    model: Record<string, unknown> | Array<Record<string, unknown>>,
    breadcrumb: breadcrumb,
): breadcrumb => breadcrumb.map(({ label, ...rest }) => ({ label: propsResolver(label, model), ...rest }))
