import moment from 'moment'
import _ from 'lodash'
import numeral from 'numeral'

import globalFnDate from './globalFnDate'
import { guid as uuid } from './id'
import { isEmptyModel } from './isEmptyModel'

export default { moment, _, numeral, $: { ...globalFnDate.getFns(), uuid, isEmptyModel } }
