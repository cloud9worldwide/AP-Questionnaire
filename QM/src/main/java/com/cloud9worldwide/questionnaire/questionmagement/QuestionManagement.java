package com.cloud9worldwide.questionnaire.questionmagement;

import android.util.Log;

import com.cloud9worldwide.questionnaire.core.CoreEngine;
import com.cloud9worldwide.questionnaire.data.ProjectData;
import com.cloud9worldwide.questionnaire.data.QuestionAnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionTypeData;
import com.cloud9worldwide.questionnaire.data.QuestionnaireAnswerData;
import com.cloud9worldwide.questionnaire.data.QuestionnaireData;
import com.cloud9worldwide.questionnaire.data.SaveAnswerData;

import java.util.ArrayList;

/**
 * Created by cloud9 on 6/3/14.
 */
public class QuestionManagement {
    private static final String debugTag = "QM";
    private final CoreEngine mEngine;

    private ArrayList<QuestionTypeData> mQuestionListData = null;
    private ArrayList<QuestionTypeData> mReQuestionListData = null;
    private int CurQuestionIndex = 0;
    private ArrayList<QuestionTypeData> mSubQuestionListData = null;
    private ArrayList<QuestionAnswerData> SubAnsListData;


    private QuestionnaireAnswerData QnAnsData;
    private boolean already_pack_question = false;
    private boolean already_pack_staff_question = false;
    private ArrayList<QuestionAnswerData> AnsListData;


    private ProjectData mProject = null;
    private QuestionnaireData mQuestionnaire = null;

    private boolean staffQustion = false;
    private String errorMsg = "";

    public boolean isStaffQustion() {
        return staffQustion;
    }

    public int getCurQuestionIndex() {
        return CurQuestionIndex;
    }
    public ArrayList<QuestionTypeData> get_questions(){
        return this.mQuestionListData;
    }
    public String getErrorMsg() {
        return errorMsg;
    }

    public QuestionManagement(CoreEngine _coreEngine,ProjectData _project,QuestionnaireData _questionnaire){
        this.mEngine        = _coreEngine;
        this.mProject       = _project;
        this.mQuestionnaire = _questionnaire;

        this.QnAnsData = new QuestionnaireAnswerData();

        this.QnAnsData.setIscustomerLocal(mEngine.globals.getIsCustomerLocal());
        this.QnAnsData.setCustomerId(String.valueOf(mEngine.globals.getContactId()));

        this.QnAnsData.setProjectId(this.mProject.getId());
        this.QnAnsData.setQuestionnaireId(this.mQuestionnaire.getId());

        this.AnsListData = new ArrayList<QuestionAnswerData>();
    }
    public void InitQuestionListData (ArrayList<QuestionTypeData> _data){
        if(_data == null)
            return;

        this.mQuestionListData = new ArrayList<QuestionTypeData>();
        this.AnsListData = new ArrayList<QuestionAnswerData>();


        ArrayList<QuestionTypeData> _tmp_sub_question_list = new ArrayList<QuestionTypeData>();
        for (int i = 0; i < _data.size(); i++) {
            QuestionTypeData question_tmp = _data.get(i);
            if(question_tmp.getParent_question_id() == -1){
                // not is sub question
                this.mQuestionListData.add(question_tmp);
            }else{
                // is sub question
                _tmp_sub_question_list.add(question_tmp);
            }
        }

        //find parent question
        for (int i = 0; i < _tmp_sub_question_list.size(); i++) {
            QuestionTypeData tmp_sq = _tmp_sub_question_list.get(i);
            QuestionTypeData tmp_pq = this.get_question_byQuestionId(tmp_sq.getParent_question_id());
            tmp_pq.setParent_question(true);
            this.udpate_quesetion(tmp_sq.getParent_question_id(),tmp_pq);
        }
        this.mSubQuestionListData = _tmp_sub_question_list;
        this.SubAnsListData = new ArrayList<QuestionAnswerData>();

        //set default SaveAnswer data
        for (int i = 0; i < this.count_questions(); i++) {
            QuestionTypeData question = this.get_question_byIndex(i);
            SaveAnswerData save_ans = new SaveAnswerData("-1","");
            ArrayList<SaveAnswerData> _ans_list = new ArrayList<SaveAnswerData>();
            _ans_list.add(save_ans);
            QuestionAnswerData question_ans = new QuestionAnswerData(String.valueOf(question.getQuestion().getId()),_ans_list);
            question_ans.setDefault(true);
            this.AnsListData.add(question_ans);
        }

        //set default sub question SaveAnswer data
        for (int i = 0; i < _tmp_sub_question_list.size(); i++) {
            QuestionTypeData question = _tmp_sub_question_list.get(i);
            SaveAnswerData save_ans = new SaveAnswerData("-1","");
            ArrayList<SaveAnswerData> _ans_list = new ArrayList<SaveAnswerData>();
            _ans_list.add(save_ans);
            QuestionAnswerData question_ans = new QuestionAnswerData(String.valueOf(question.getQuestion().getId()),_ans_list);
            question_ans.setDefault(true);
            this.SubAnsListData.add(question_ans);
        }
    }
    public QuestionTypeData get_question(){
        if(CurQuestionIndex >= this.count_questions() || CurQuestionIndex < 0)
            return null;
        QuestionTypeData _data = this.mQuestionListData.get(CurQuestionIndex);
        return _data;
    }
    public QuestionTypeData get_sub_question(int question_id){
        QuestionTypeData parent_question = this.get_question();
        for (int i = 0; i < this.mSubQuestionListData.size(); i++) {
            QuestionTypeData tmp_q = this.mSubQuestionListData.get(i);
            if(tmp_q.getQuestion().getId() == question_id && tmp_q.getParent_question_id() == parent_question.getQuestion().getId()) {
                return tmp_q;
            }
        }
        return  null;
    }
    public synchronized boolean save_answer(ArrayList<SaveAnswerData> _save_ans_ist){
        QuestionTypeData curQuestion = this.get_question();
        for (int i = 0; i < AnsListData.size(); i++) {
            if(curQuestion.getQuestion().getId() == Integer.parseInt(AnsListData.get(i).getQuestionId())){
                QuestionAnswerData question_ans = new QuestionAnswerData(String.valueOf(curQuestion.getQuestion().getId()),_save_ans_ist);
                question_ans.setDefault(false);
                this.AnsListData.set(i, question_ans);
                return true;
            }
        }
        this.errorMsg = "Parent question not found";
        return  false;
    }
    public synchronized boolean save_answer(ArrayList<SaveAnswerData> _save_ans_ist,int question_id){
        QuestionTypeData curParentQuestion = this.get_question();
        QuestionTypeData curSubQuestion = this.get_sub_question(question_id);
        if(curParentQuestion.getQuestion().getId() == curSubQuestion.getParent_question_id()){
            for (int i = 0; i < this.SubAnsListData.size(); i++) {
                QuestionAnswerData tmp_sub_ans = this.SubAnsListData.get(i);
                if(Integer.parseInt(tmp_sub_ans.getQuestionId()) == question_id ){
                    tmp_sub_ans.setAnswer(_save_ans_ist);
                    tmp_sub_ans.setDefault(false);
                    this.SubAnsListData.set(i,tmp_sub_ans);
                    return true;
                }
            }
        }else{
            this.errorMsg = "Parent question id not match";
        }
        return false;
    }
    public void clear_answer(){
        QuestionTypeData curQuestion = this.get_question();
        for (int i = 0; i < AnsListData.size(); i++) {
            if(curQuestion.getQuestion().getId() == Integer.parseInt(AnsListData.get(i).getQuestionId())){
                SaveAnswerData save_ans = new SaveAnswerData("-1","");
                ArrayList<SaveAnswerData> _ans_list = new ArrayList<SaveAnswerData>();
                _ans_list.add(save_ans);
                QuestionAnswerData question_ans = new QuestionAnswerData(String.valueOf(curQuestion.getQuestion().getId()),_ans_list);
                question_ans.setDefault(true);
                this.QnAnsData.getAnswers().set(i, question_ans);
            }
        }
    }
    public void clear_sub_answer(int question_id){
        QuestionTypeData curParentQuestion = this.get_question();
        QuestionTypeData curSubQuestion = this.get_sub_question(question_id);
        if(curParentQuestion.getQuestion().getId() == curSubQuestion.getParent_question_id()){
            for (int i = 0; i < this.SubAnsListData.size(); i++) {
                QuestionAnswerData tmp_sub_ans = this.SubAnsListData.get(i);
                if(Integer.parseInt(tmp_sub_ans.getQuestionId()) == question_id ){
                    SaveAnswerData save_ans = new SaveAnswerData("-1","");
                    ArrayList<SaveAnswerData> _ans_list = new ArrayList<SaveAnswerData>();
                    _ans_list.add(save_ans);
                    tmp_sub_ans.setAnswer(_ans_list);
                    tmp_sub_ans.setDefault(true);
                    this.SubAnsListData.set(i,tmp_sub_ans);
                }
            }
        }
    }
    public synchronized boolean move_next(){
        CurQuestionIndex++;
        if(CurQuestionIndex >= this.count_questions()){
            CurQuestionIndex = this.count_questions() - 1;
            return false;
        }
        return true;
    }
    public synchronized boolean move_back(){
        CurQuestionIndex--;
        Log.e("index",String.valueOf(CurQuestionIndex));
        if(CurQuestionIndex < 0){
            CurQuestionIndex = 0;
            return false;
        }
        return true;
    }
    public int count_questions(){
        return  this.mQuestionListData.size();
    }
    private QuestionTypeData get_question_byIndex(int inx){
        if( inx >= this.count_questions() || inx < 0)
            return null;

        QuestionTypeData _data = this.mQuestionListData.get(inx);
        return _data;
    }
    private  QuestionTypeData get_question_byQuestionId(int id){
        for (int i = 0; i < this.mQuestionListData.size(); i++) {
            QuestionTypeData tmp = this.mQuestionListData.get(i);
            if(tmp.getQuestion().getId() == id){
                return  tmp;
            }

        }
        return null;
    }
    private void udpate_quesetion(int question_id,QuestionTypeData newData){
        for (int i = 0; i < this.mQuestionListData.size(); i++) {
            QuestionTypeData tmp = this.mQuestionListData.get(i);
            if(tmp.getQuestion().getId() == question_id){
                this.mQuestionListData.set(i,newData);
            }

        }
    }
    public ArrayList<QuestionTypeData> CheckQuestionNotAns(){
        this.mReQuestionListData = null;
        ArrayList<QuestionTypeData> tmp_question_not = new ArrayList<QuestionTypeData>();
        for (int i = 0; i < AnsListData.size(); i++) {
            if(AnsListData.get(i).isDefault()){
                tmp_question_not.add(this.get_question_byQuestionId(Integer.parseInt(AnsListData.get(i).getQuestionId())));
            }
        }
        this.mReQuestionListData = tmp_question_not;
        return tmp_question_not;
    }
    public ArrayList<QuestionTypeData> get_all_questions_not_ans(){
        return this.mReQuestionListData;
    }
    public void redo_question_not_ans(){
        this.mQuestionListData = this.mReQuestionListData;
        CurQuestionIndex = 0;
    }
    public void redo_question_not_ansAtIndex(int index){
        this.mQuestionListData = this.mReQuestionListData;
        CurQuestionIndex = index;
    }
    public synchronized boolean pack_question_ans_data(){
        if(!this.already_pack_question){
            //get all sub question ans , except default ans
            ArrayList<QuestionAnswerData> sub_ans_list = this.SubAnsListData;
            for (int i = 0; i < sub_ans_list.size(); i++) {
                QuestionAnswerData tmp = sub_ans_list.get(i);
                if(!tmp.isDefault()){
                    AnsListData.add(tmp);
                }
            }

            this.QnAnsData.setAnswers(AnsListData);
            this.already_pack_question = true;
            this.SubAnsListData = new ArrayList<QuestionAnswerData>();
        }
        return true;
    }
    public synchronized boolean InitStaffQuestionListData(ArrayList<QuestionTypeData> _data){
        if(this.already_pack_question) {
            this.InitQuestionListData(_data);
            CurQuestionIndex = 0;
            staffQustion = true;
            return  true;
        }else{
            this.errorMsg = "Question answer data not packing";
            return  false;
        }
    }
    public synchronized boolean pack_staff_question_ans_data(){
        if(!this.already_pack_staff_question) {
            ArrayList<QuestionAnswerData> sub_ans_list = this.SubAnsListData;
            for (int i = 0; i < sub_ans_list.size(); i++) {
                QuestionAnswerData tmp = sub_ans_list.get(i);
                if(!tmp.isDefault()){
                    AnsListData.add(tmp);
                }
            }

            this.QnAnsData.setStaffanswers(AnsListData);
            this.already_pack_staff_question = true;
            this.SubAnsListData = new ArrayList<QuestionAnswerData>();
        }
        return true;
    }
    public QuestionnaireAnswerData get_questionnaire_ans_data(){
        if(this.already_pack_question && this.already_pack_staff_question)
            return this.QnAnsData;
        else
            return  null;
    }
    public synchronized QuestionAnswerData get_answer(){
        QuestionAnswerData _ans = this.AnsListData.get(CurQuestionIndex);
        if(_ans.isDefault())
            return null;
        else
            return _ans;
    }
    public synchronized QuestionAnswerData get_sub_answer(int question_id){
        for (int i = 0; i < this.SubAnsListData.size(); i++) {
            QuestionAnswerData tmp_sub_ans = this.SubAnsListData.get(i);
            if(Integer.parseInt(tmp_sub_ans.getQuestionId()) == question_id ){
                if(!tmp_sub_ans.isDefault())
                    return tmp_sub_ans;
            }
        }
        return null;
    }
}
