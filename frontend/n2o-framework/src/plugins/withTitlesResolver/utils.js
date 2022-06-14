import { isEmpty } from 'lodash'

import propsResolver from '../../utils/propsResolver'

export const resolveTitles = (titlesObj, models) => {
    if (isEmpty(models.datasource)) { return titlesObj }

    return propsResolver(titlesObj, models.datasource[0])
}
