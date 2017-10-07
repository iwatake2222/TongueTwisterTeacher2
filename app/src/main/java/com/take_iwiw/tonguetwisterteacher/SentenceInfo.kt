package com.take_iwiw.tonguetwisterteacher
import java.io.Serializable;

/**
 * Created by tak on 2017/10/05.
 */
class SentenceInfo(val dbId: Int, val languageId: Int, val sentenceMain: String, val sentenceSub: String, val pronunciation: String, var cntAll: Int, var cntSuccess: Int, var timeRecord: Double) : Serializable {
    constructor(dbId: Int, languageId: Int, sentenceMain: String, sentenceSub: String, pronunciation: String) : this(dbId, languageId, sentenceMain, sentenceSub, pronunciation, 0, 0, 3599.99)

    fun getSentenceAll(): String {
        var str: String = sentenceMain + BR + "- END -" + BR + BR  + sentenceSub
        return str
    }

    fun getRecordNumString(): String {
        return Integer.toString(cntSuccess) + " / " + Integer.toString(cntAll)
    }
    fun getRecordTimeString(): String {
        val timeRecordInt = timeRecord.toInt()
        return String.format("%02d:%02d.%02d", (timeRecordInt/60)%60, timeRecordInt%60, ((timeRecord - timeRecordInt) * 100).toInt() )
    }

    companion object {
        private val BR = System.getProperty("line.separator")
        fun populateDefaultSentences(): Array<SentenceInfo> {
            var sentences: Array<SentenceInfo> = arrayOf(
                    /* English */
                    SentenceInfo(0, 0, "She sells seashells by the seashore.", "", ""),
                    SentenceInfo(0, 0, "Peter Piper picked a peck of pickled peppers.", "", ""),
                    SentenceInfo(0, 0, "Betty Botter bought some butter.", "", ""),
                    SentenceInfo(0, 0, "Red lorry, yellow lorry. Red lorry, yellow lorry. Red lorry, yellow lorry.", "", ""),
                    SentenceInfo(0, 0, "Red leather yellow leather. Red leather yellow leather. Red leather yellow leather.", "", ""),
                    SentenceInfo(0, 0, "Really leery, rarely Larry. Really leery, rarely Larry. Really leery, rarely Larry.", "", ""),
                    SentenceInfo(0, 0, "I scream you scream we all scream for ice cream.", "", ""),
                    SentenceInfo(0, 0, "Fuzzy wuzzy was a bear. Fuzzy wuzzy had no hair. Fuzzy wuzzy wasn't very fuzzy, was he?", "", ""),
                    SentenceInfo(0, 0, "Whether the weather is warm, whether the weather is hot, we have to put up with the weather, whether we like it or not.", "", ""),
                    SentenceInfo(0, 0, "Friendly fleas and fireflies.", "", ""),
                    SentenceInfo(0, 0, "Not these things here, but those things there.", "", ""),
                    SentenceInfo(0, 0, "Eleven owls licked eleven little liquorice lollipops.", "", ""),
                    SentenceInfo(0, 0, "Greek grapes, Greek grapes, Greek grapes.", "", ""),
                    SentenceInfo(0, 0, "Kitty caught the kitten in the kitchen.", "", ""),
                    SentenceInfo(0, 0, "Zebras zig and zebras zag.", "", ""),
                    SentenceInfo(0, 0, "If two witches were watching two watches, which witch would watch which watch?", "", ""),
                    SentenceInfo(0, 0, "The big bug bit the little beetle, but the little beetle bit the big bug back.", "", ""),
                    SentenceInfo(0, 0, "If you want to buy, buy, if you don't want to buy, bye bye!", "", ""),
                    SentenceInfo(0, 0, "The blue bluebird blinks.", "", ""),
                    SentenceInfo(0, 0, "A tricky frisky snake with sixty super scaly stripes.", "", ""),
                    SentenceInfo(0, 0, "I can think of six thin things, but I can think of six thick things too.", "", ""),
                    SentenceInfo(0, 0, "Toy phone, Toy phone, Toy phone. Toy phone, Toy phone, Toy phone. Toy phone, Toy phone, Toy phone.", "", ""),
                    SentenceInfo(0, 0, "Give papa a cup of proper coffee in a copper coffee cup.", "", ""),
                    SentenceInfo(0, 0, "How much wood would a woodchuck chuck if a woodchuck could chuck wood", "", ""),
                    SentenceInfo(0, 0, "A big black bug bit a big black dog on his big black nose.", "", ""),
                    SentenceInfo(0, 0, "Quick kiss, quick kiss, quick kiss.", "", ""),
                    SentenceInfo(0, 0, "Can you can a can as a canner can can a can?", "", ""),
                    SentenceInfo(0, 0, "Fresh fried fish, fish fresh fried, fried fish fresh, fish fried fresh.", "", ""),
                    SentenceInfo(0, 0, "How many boards. Could the Mongols hoard. If the Mongol hordes got bored?", "", ""),
                    SentenceInfo(0, 0, "Denise sees the fleece, Denise sees the fleas. At least Denise could sneeze and feed and freeze the fleas.", "", ""),
                    SentenceInfo(0, 0, "The thirty-three thieves thought that they thrilled the throne throughout Thursday.", "", ""),
                    SentenceInfo(0, 0, "I wish to wish the wish you wish to wish, but if you wish the wish the witch wishes, I won't wish the wish you wish to wish.", "", ""),
                    SentenceInfo(0, 0, "How many cans can a cannibal nibble if a cannibal can nibble cans? As many cans as a cannibal can nibble if a cannibal can nibble cans.", "", ""),
                    SentenceInfo(0, 0, "Fresh fried fish, Fish fresh fried, Fried fish fresh, Fish fried fresh.", "", ""),
                    SentenceInfo(0, 0, "She sells seashells by the seashore. The shells that she sells are seashells I'm sure. So if she sells seashells by the seashore, I'm sure that the shells are seashore shells.", "", ""),
                    SentenceInfo(0, 0, "Peter Piper picked a peck of pickled peppers. A peck of pickled peppers Peter Piper picked. If Peter Piper picked a peck of pickled peppers. Where's the peck of pickled peppers Peter Piper picked?", "", ""),
                    SentenceInfo(0, 0, "Betty Botter bought some butter. But she said the butter's bitter. If I put it in my batter, it will make my batter bitter. But a bit of better butter will make my batter better. So 'twas better Betty Botter bought a bit of better butter.", "", ""),
                    SentenceInfo(0, 0, "Yellow butter, purple jelly, red jam, black bread. Spread it thick, say it quick! Yellow butter, purple jelly, red jam, black bread. Spread it thicker, say it quicker! Yellow butter, purple jelly, red jam, black bread. Don't eat with your mouth full!", "", ""),
                    SentenceInfo(0, 0, "How much wood could Chuck Woods' woodchuck chuck, if Chuck Woods' woodchuck could and would chuck wood? If Chuck Woods' woodchuck could and would chuck wood, how much wood could and would Chuck Woods' woodchuck chuck?  Chuck Woods' woodchuck would chuck, he would, as much as he could, and chuck as much wood as any woodchuck would, if a woodchuck could and would chuck wood.", "", ""),
                    SentenceInfo(0, 0, "Something in a thirty-acre thermal thicket of thorns and thistles thumped and thundered threatening the three-D thoughts of Matthew the thug - although, theatrically, it was only the thirteen-thousand thistles and thorns through the underneath of his thigh that the thirty year old thug thought of that morning.", "", ""),

                    /* Japanese */
                    SentenceInfo(0, 1, "隣の客は、よく柿食う客だ", "となりのきゃくは、よくかきくうきゃくだ" + BR +  "To na ri no kya ku wa, yo ku ka ki kuu kya ku da", "トナリノキャクハ ヨクカキクウキャクダ"),
                    SentenceInfo(0, 1, "赤巻紙、青巻紙、黄巻紙", "あかまきがみ、あおまきがみ、きまきがみ" + BR + "A ka ma ki ga mi, a o ma ki ga mi, ki ma ki ga mi", "アカマキガミ アオマキガミ キマキガミ"),
                    SentenceInfo(0, 1, "生麦生米生卵", "なまむぎ なまごめ なまたまご" + BR + "Na ma mu gi, na ma go me, na ma ta ma go", "ナマムギ ナマゴメ ナマタマゴ"),
                    SentenceInfo(0, 1, "バスガス爆発、バスガス爆発、バスガス爆発", "ばすがすばくはつ" + BR + "Ba su ga su ba ku ha tsu", "バスガスバクハツ バスガスバクハツ バスガスバクハツ"),
                    SentenceInfo(0, 1, "東京特許許可局", "とうきょうとっきょきょかきょく" + BR + "Tou kyou to kkyo kyo ka kyo ku", "トウキョウトッキョキョカキョク"),
                    SentenceInfo(0, 1, "赤パジャマ、黄パジャマ、茶パジャマ", "あかぱじゃま、きぱじゃま、ちゃぱじゃま" + BR + "A ka pa ja ma, ki pa ja ma, cha pa ja ma", "アカパジャマ キパジャマ チャパジャマ"),
                    SentenceInfo(0, 1, "新春シャンソンショー、新春シャンソンショー、新春シャンソンショー", "しんしゅんしゃんそんしょー" + BR + "Shi n shu n sha n so n sho-", "シンシュンシャンソンショー シンシュンシャンソンショー シンシュンシャンソンショー"),
                    SentenceInfo(0, 1, "かえるぴょこぴょこ三ぴょこぴょこ、あわせてぴょこぴょこ六ぴょこぴょこ", "かえるぴょこぴょこみぴょこぴょこ、あわせてぴょこぴょこむぴょこぴょこ" + BR + "Ka e ru pyo ko pyo ko mi pyo ko pyo ko, a wa se te pyo ko pyo ko mu pyo ko pyo ko", "カエルピョコピョコミピョコピョコ アワセテピョコピョコムピョコピョコ"),
                    SentenceInfo(0, 1, "赤カマキリ、青カマキリ、黄カマキリ", "あかかまきり、あおかまきり、きかまきり" + BR + "A ka ka ma ki ri, a o ka ma ki ri, ki ka ma ki ri", "アオカマキリ アオカマキリ キカマキリ"),
                    SentenceInfo(0, 1, "スモモも桃も桃のうち、桃もスモモも桃のうち", "すもももももももものうち、もももすももももものうち" + BR + "Su mo mo mo mo mo mo mo mo no u chi, mo mo mo su mo mo mo mo mo no u chi", "スモモモモモモモモノウチ モモモスモモモモモノウチ"),
                    SentenceInfo(0, 1, "肩たたき機、肩たたき機、肩たたき機", "かたたたきき" + BR + "Ka ta ta ta ki ki", "カタタタキキ カタタタキキ カタタタキキ"),
                    SentenceInfo(0, 1, "派出所、派出所、派出所", "はしゅつじょ" + BR + "Ha shu tsu jo", "ハシュツジョ ハシュツジョ ハシュツジョ"),
                    SentenceInfo(0, 1, "老若男女、老若男女、老若男女", "ろうにゃくなんにょ" + BR + "Rou nya ku na n nyo", "ロウニャクナンニョ、ロウニャクナンニョ、ロウニャクナンニョ"),
                    SentenceInfo(0, 1, "マサチューセッツ州", "まさちゅーせっつしゅう" + BR + "Ma sa chu- se ttsu shuu", "マサチューセッツシュウ"),
                    SentenceInfo(0, 1, "骨粗鬆症、骨粗鬆症、骨粗鬆症", "こつそしょうしょう" + BR + "Ko tsu so shou shou", "コツソショウショウ コツソショウショウ コツソショウショウ"),
                    SentenceInfo(0, 1, "公序良俗、公序良俗、公序良俗", "こうじょりょうぞく" + BR + "Kou jo ryou zo ku", "コウジョリョウゾク コウジョリョウゾク コウジョリョウゾク"),
                    SentenceInfo(0, 1, "除雪車除雪作業中", "じょせつしゃじょせつさぎょうちゅう" + BR + "Jo se tsu sha jo se tsu sa gyou chuu", "ジョセツシャジョセツサギョウチュウ"),
                    SentenceInfo(0, 1, "高架橋橋脚", "こうかきょうきょうきゃく" + BR + "Kou ka kyou kyou kya ku", "コウカキョウキョウキャク"),
                    SentenceInfo(0, 1, "貨客船の旅客", "かきゃくせんのりょかく" + BR + "Ka kya ku se n no ryo ka ku", "カキャクセンノリョカク"),
                    SentenceInfo(0, 1, "旅客機の旅客", "りょかくきのりょかく" + BR + "Ryo ka ku ki no ryo ka ku", "リョカクキノリョカク"),
                    SentenceInfo(0, 1, "骨粗鬆症訴訟勝訴", "こつそしょうしょうそしょうしょうそ"+ BR + "Ko tsu so shou shou so shou shou so", "コツソショウショウソショウショウソ"),
                    SentenceInfo(0, 1, "老若男女骨粗鬆症", "ろうにゃくなんにょこつそしょうしょう" + BR + "Rou nya ku na n nyo ko tsu so shou shou", "ロウニャクナンニョコツソショウショウ"),
                    SentenceInfo(0, 1, "この釘はひきぬきにくい釘だ", "このくぎはひきぬきにくいくぎだ" + BR + "Ko no ku gi wa hi ki nu ki ni ku i ku gi da", "コノクギハヒキヌキニクイクギダ"),
                    SentenceInfo(0, 1, "隣の竹藪に竹立てかけたの誰だ", "となりのたけやぶにたけたてかけたのだれだ" + BR + "To na ri no ta ke ya bu ni ta ke ta te ka ke ta no da re da", "トナリノタケヤブニタケタテカケタノダレダ"),
                    SentenceInfo(0, 1, "庭には二羽、裏庭には二羽、鶏がいる", "にわにはにわ、うらにわにはにわ、にわとりがいる" + BR + "Ni wa ni wa ni wa, u ra ni wa ni wa ni wa, ni wa to ri ga i ru", "ニワニハニワ、ウラニワニハニワ、ニワトリガイル"),
                    SentenceInfo(0, 1, "ブスバスガイド、バスガス爆発", "ぶすばすがいど、ばすがすばくはつ" + BR + "Bu su ba su ga i do, ba su ga su ba ku ha tsu", "ブスバスガイド バスガスバクハツ"),
                    SentenceInfo(0, 1, "国語、熟語、述語、主語", "こくご、じゅくご、じゅつご、しゅご" + BR + "Ko ku go, ju ku go, ju tsu go, shu go", "コクゴ ジュクゴ ジュツゴ シュゴ"),
                    SentenceInfo(0, 1, "第一著者、第二著者、第三著者", "だいいちちょしゃ、だいにちょしゃ、だいさんちょしゃ、" + BR + "Da i i chi cho sha, da i ni cho sha, da i san cho sha", "ダイイチチョシャ ダイニチョシャ ダイサンチョシャ"),
                    SentenceInfo(0, 1, "打者、走者、勝者、走者一掃", "だしゃ、そうしゃ、しょうしゃ、そうしゃいっそう" + BR + "Da sha, sou sha, shou sha, sou sha i ssou", "ダシャ ソウシャ ショウシャ ソウシャイッソウ"),
                    SentenceInfo(0, 1, "魔術師魔術修行中", "まじゅつしまじゅつしゅぎょうちゅう" + BR + "Ma ju tsu shi ma ju tsu shu gyou chuu", "マジュツシマジュツシュギョウチュウ"),
                    SentenceInfo(0, 1, "右目右耳、右耳右目", "みぎめみぎみみ、みぎみみみぎめ" + BR + "Mi gi me mi gi mi mi, mi gi mi mi mi gi me", "ミギメミギミミ ミギミミミギメ"),
                    SentenceInfo(0, 1, "地味な爺やの自慢の地酒", "じみなじいやのじまんのじざけ" + BR + "Ji mi na jii ya no ji ma n no ji za ke", "ジミナジイヤノジマンノジザケ"),
                    SentenceInfo(0, 1, "この竹垣に竹立てかけたのは、竹立てかけたかったから、竹立てかけた", "このたけがきにたけたてかけたのは、たけたてかけたかったから、たけたてかけた" + BR +
                            "Ko no ta ke ga ki ni ta ke ta te ka ke ta no wa, ta ke ta te ka ke ta ka tta ka ra, ta ke ta te ka ke ta", "コノタケガキニタケタテカケタノハ タケタテカケタカッタカラ タケタテカケタ"),
                    SentenceInfo(0, 1, "どじょうにょろにょろ三にょろにょろ、あわせてにょろにょろ六にょろにょろ", "どじょうにょろにょろみにょろにょろ、あわせてにょろにょろむにょろにょろ" + BR +
                            "Do jou nyo ro nyo ro mi nyo ro nyo ro, a wa se te nyo ro nyo ro mu nyo ro nyo ro", "ドジョウニョロニョロミニョロニョロ アワセテニョロニョロムニョロニョロ"),
                    SentenceInfo(0, 1, "空虚な九州空港の究極高級航空機", "くうきょなきゅうしゅうくうこうのきゅうきょくこうきゅうこうくうき" + BR +
                            "Kuu kyo na kyuu shuu kuu kou no kyuu kyo ku kou kyuu kou kuu ki", "クウキョナキュウシュウクウコウノキュウキョクコウキュウコウクウキ"),
                    SentenceInfo(0, 1, "伝染病予防病院予防病室、伝染病予防法", "でんせんびょうよぼうびょういんよぼうびょうしつ、でんせんびょうよぼうほう" + BR +
                            "De n se n byou yo bou byou i n yo bou byou shi tsu, de n se n byou yo bou hou", "デンセンビョウヨボウビョウインヨボウビョウシツ デンセンビョウヨボウホウ"),
                    SentenceInfo(0, 1, "坊主が屏風に上手に坊主の絵を描いた", "ぼうずがびょうぶにじょうずにぼうずのえをかいた" + BR +
                            "Bou zu ga byou bu ni jou zu ni bou zu no e wo ka i ta", "ボウズガビョウブニジョウズニボウズノエヲカイタ"),
                    SentenceInfo(0, 1, "東京特許許可局長今日急遽休暇許可拒否", "とうきょうとっきょきょかきょくちょうきょうきゅうきょきゅうかきょかきょひ" + BR +
                            "Tou kyou to kkyo kyo ka kyo ku chou kyou kyuu kyo kyuu ka kyo ka kyo hi", "トウキョウトッキョキョカキョクチョウキョウキュウキョキュウカキョカキョヒ"),
                    SentenceInfo(0, 1, "新進シャンソン歌手総出演新春シャンソンショー", "しんしんしゃんそんかしゅそうしゅつえんしんしゅんしゃんそんしょー" + BR +
                            "Shi n shi n sha n so n ka shu sou shu tsu e n shi n shu n sha n so n sho-", "シンシンシャンソンカシュソウシュツエンシンシュンシャンソンショー"),
                    SentenceInfo(0, 1, "客が柿食や飛脚が柿食う、飛脚が柿食や客も柿食う、客も飛脚もよく柿食う客飛脚", "きゃくがかきくやひきゃくがかきくう、ひきゃくがかきくやきゃくもかきくう、きゃくもひきゃくもよくかきくうきゃくひきゃく" + BR +
                            "Kya ku ga ka ki ku ya hi kya ku ga ka ki kuu, hi kya ku ga ka ki ku ya kya ku mo ka ki kuu, kya ku mo hi kya ku mo yo ku ka ki kuu kya ku hi kya ku",
                            "キャクガカキクヤヒキャクガカキクウ ヒキャクガカキクヤキャクモカキクウ キャクモヒキャクモヨクカキクウキャクヒキャク"),
                    SentenceInfo(0, 1, "美術室技術室手術室、美術準備室技術準備室手術準備室、美術助手技術助手手術助手", "びじゅつしつぎじゅつしつしゅじゅつしつ、びじゅつじゅんびしつぎじゅつじゅんびしつしゅじゅつじゅんびしつ、びじゅつじょしゅぎじゅつじょしゅしゅじゅつじょしゅ" + BR +
                            "Bi ju tsu shi tsu gi ju tsu shi tsu shu ju tsu shi tsu, bi ju tsu ju n bi shi tsu gi ju tsu ju n bi shi tsu shu ju tsu ju n bi shi tsu, bi ju tsu jo shu gi ju tsu jo shu shu ju tsu jo shu",
                            "ビジュツシツギジュツシツシュジュツシツ ビジュツジュンビシツギジュツジュンビシツシュジュツジュンビシツ ビジュツジョシュギジュツジョシュシュジュツジョシュ"),
                    SentenceInfo(0, 1, "豚が豚をぶったらぶたれた豚がぶった豚をぶったのでぶった豚とぶたれた豚がぶっ倒れた", "ぶたがぶたをぶったらぶたれたぶたがぶったぶたをぶったのでぶったぶたとぶたれたぶたがぶったおれた" + BR +
                            "Bu ta ga bu ta wo bu tta ra bu ta re ta bu ta ga bu tta bu ta wo bu tta no de bu tta bu ta to bu ta re ta bu ta ga bu tta o re ta",
                            "ブタガブタヲブッタラブタレタブタガブッタブタヲブッタノデブッタブタトブタレタブタガブッタオレタ"),
                    SentenceInfo(0, 1, "可逆反応の逆不可逆反応、不可逆反応の逆可逆反応、可逆反応も不可逆反応も化学反応", "かぎゃくはんのうのぎゃくふかぎゃくはんのう、ふかぎゃくはんのうのぎゃくかぎゃくはんのう、かぎゃくはんのうもふかぎゃくはんのうもかがくはんのう" + BR +
                            "Ka gya ku ha n nou no gya ku hu ka gya ku ha n nou, hu ka gya ku ha n nou no gya ku hu ka gya ku ha n nou, ka gya ku ha n nou mo hu ka gya ku ha n nou mo ka ga ku ha n nou",
                            "カギャクハンノウノギャクフカギャクハンノウ フカギャクハンノウノギャクカギャクハンノウ カギャクハンノウモフカギャクハンノウモカガクハンノウ")
            )
            return sentences
        }
    }
}
