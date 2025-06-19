import moment from 'moment/moment'
// eslint-disable-next-line no-restricted-imports
import _ from 'lodash'
import numeral from 'numeral'

import globalFnDate from './globalFnDate'
import { guid as uuid } from './id'
import { isEmptyModel } from './isEmptyModel'

export const functions = { moment, _, numeral, $: { ...globalFnDate.getFns(), uuid, isEmptyModel } }
export default { moment, _, numeral, $: { ...globalFnDate.getFns(), uuid, isEmptyModel } }
