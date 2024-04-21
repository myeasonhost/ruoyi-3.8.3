import Base64 from 'crypto-js/enc-base64'
import Latin1 from 'crypto-js/enc-latin1'

import * as des from './des'

const dnP = (vk, str) => {
  const s = Latin1.stringify(Base64.parse(str))
  const p = des.des(vk, s, 0, 0, null, 1)
  return p
}

const dnD = (rk, str) => {
  const s = Latin1.stringify(Base64.parse(str))
  const d = des.des(rk, s, 0, 0, null, 1)
  return d
}

const key = 'fT6phq0wkOPRlAoyToidAnkogUV7ttGo'

// http://52.52.144.209:85/?data=ymAZ32jWHowzmBlwjR9ZL9Tcmlmw7C439wW1OctU65h27h27wdkAQtNF5R3FGYY3LsLxJe4tXtSU4T6mfVEadKgZ2TiiLD7s%20w1niCDe9pU%3D&key=x7ujh35XKQ%20xFGfzlqPP%2F8TuddEeJGG0&locale=en-US
export default function parseData (d, p) {
  const data = decodeURIComponent(d.replaceAll('%20', '+'))
  const rpwds = decodeURIComponent(p.replaceAll('%20', '+'))
  const recData = data
  const dpwd = dnP(key, rpwds)
  const drk = dpwd.substring(0, 8)
  const dm = dnD(drk, recData)
  console.log('解密出来得数据', dm)
  return dm
}
