/**
 * TODOファイルから他人マターのジョブを抽出して送信メールを作成する。
 */

// ----------------------------------------------------------------------------
// プロパティ設定
// ----------------------------------------------------------------------------

/** 読込対象ファイルパス */
final String todo_file = "/* TODOファイルのパス */"

/** メールアドレス確認用正規表現($は対象外) */
final String reg_mail = "([a-zA-Z0-9.!#%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)";

// ----------------------------------------------------------------------------
// メイン処理
// ----------------------------------------------------------------------------
String       person    = null;
String       mail      = null;
StringBuffer task_list = null;

boolean is_target = false;

// ファイル読込
new File(todo_file).eachLine("MS932", {
  // itをlineとして扱う。
  line ->

  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
  // ファイル読込処理
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

  // 「■(担当者名): メールアドレス」に該当する行を抽出
  if(line ==~ ("^(■)([^:]+)(:[ ]*)" + reg_mail)){
    person = ((line =~ /[^:■]+/)[0]).trim()
    mail   = ((line =~ /[^:■]+/)[1]).trim()

    task_list = new StringBuffer();
    is_target = true;
    return;
  }
  if(is_target){
    // 待ち受けタスクリストの終了
    if(line ==~ ('^$')){
      def subject = "[Remind] ${person}にお願いしていること";
      def message = make_message(person, task_list.toString());

      // メール送信
      send_mail(mail, subject, message);

      // 変数初期化
      person    = null;
      mail      = null;
      task_list = null;
      is_target = false;

      return;
    }

    // タスクリストを追加
    task_list.append(line+"\r\n");
  }
});

// ----------------------------------------------------------------------------
// メール本文作成
// ----------------------------------------------------------------------------
def make_message(person, task_list){
  String message =  """
${person}

おつかれさまです。
このメールは自動で送信を行っております。

現在、${person}には下記のお願いをしております。
ご確認の上、対応をお願いいたします。

${task_list}

以上、よろしくお願いいたします。

"""
  // メールの改行コードは\r\n
  return message.replaceAll("\n", "\r\n");

}

// ----------------------------------------------------------------------------
// メール送信ロジック
// ----------------------------------------------------------------------------
def send_mail(to_addr, subject, message){
  @Grab(group='javax.activation', module='activation', version='1.1.1')
  @Grab(group='javax.mail'      , module='mail'      , version='1.4.7')
  @GrabConfig(systemClassLoader=true)


  def ant = new AntBuilder()
  ant.mail(
    mailhost        : "smtp.mail.serv.addr",
    messagemimetype : "text/plain",
    from            : "from@mail.address",
    tolist          : to_addr,
    subject         : subject,
    message         : message,
    charset         : 'UTF-8'
  )
}
