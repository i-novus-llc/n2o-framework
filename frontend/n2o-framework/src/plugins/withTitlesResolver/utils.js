import { isEmpty } from 'lodash'

import propsResolver from '../../utils/propsResolver'

export const resolveTitles = (titlesObj, datasourceModel) => {
    if (isEmpty(datasourceModel)) { return titlesObj }

    return propsResolver(titlesObj, datasourceModel)
}
