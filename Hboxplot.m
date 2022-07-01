function Hboxplot
for m = 1:10
    format long g
    H1 = [];
    H2 = [];
    H3 = [];
    H4 = [];
    H5 = [];
    H6 = [];
    H7 = [];
    H8 = [];
    for i=m:m
        for j = 1:12
        filename=['result4\num' num2str(i) '\test' num2str(j)];
        disp(filename)
        ex1 = importdata(strcat(filename,'\aosnsga3-rwa.txt'));
        ex2 = importdata(strcat(filename,'\nsga3-rwa.txt'));
        ex3 = importdata(strcat(filename,'\aosssnsga2-rwa.txt'));
        ex4 = importdata(strcat(filename,'\aosnsga2-rwa.txt'));
        ex5 = importdata(strcat(filename,'\ssnsga2-rwa.txt'));
        ex6 = importdata(strcat(filename,'\nsga2-rwa.txt'));
        ex7 = importdata(strcat(filename,'\moead-rwa.txt'));
        ex8 = importdata(strcat(filename,'\spea2-rwa.txt'));
        q=[ex1;ex2;ex3;ex4;ex5;ex6;ex7;ex8];
        m1=max(q);
        m2=min(q);
        r=m1-(m1-m2)*0.1;
        ref = [r(1,1),r(1,2),1];
        h1= P_evaluate('HV',ex1,ref);
        h2= P_evaluate('HV',ex2,ref);
        h3= P_evaluate('HV',ex3,ref);
        h4= P_evaluate('HV',ex4,ref);
        h5= P_evaluate('HV',ex5,ref);
        h6= P_evaluate('HV',ex6,ref);
        h7= P_evaluate('HV',ex7,ref);
        h8= P_evaluate('HV',ex8,ref);
        H1=[H1;h1];
        H2=[H2;h2];
        H3=[H3;h3];
        H4=[H4;h4];
        H5=[H5;h5];
        H6=[H6;h6];
        H7=[H7;h7];
        H8=[H8;h8];
        end
    end
    result = [H1,H2,H3,H4,H5,H6,H7,H8];
    xlswrite('result.xls',result,m,'B2');
    display('aosnsga3:')
    H1
    display('nsga3:')
    H2
    display('aosssnsga2:')
    H3
    display('aosnsga2:')
    H4
    display('ssnsga2:')
    H5
    display('nsga2:')
    H6
    display('moead:')
    H7
    display('spea2:')
    H8
end
end