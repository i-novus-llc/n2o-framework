import { textResolver } from '../textResolver'

import { breadcrumb } from './const'

export const breadcrumbResolver = (
    model: object| object[],
    breadcrumb: breadcrumb,
): breadcrumb => breadcrumb.map(({ label, ...rest }) => ({ label: textResolver(model, label), ...rest }))
